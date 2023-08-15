package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberInfoGeneralDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.mapper.MemberMapper;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import com.astrodust.familyStoryBook_backend.utils.AuthenticatedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@Api(value = "MemberRestAPI")
public class MemberController {
	private final MemberService memberService;
	private final FamilyService familyService;
	private final MemberMapper memberMapper;
	private final AuthenticatedUser authenticatedUser;

	@Autowired
	public MemberController(MemberService memberService, FamilyService familyService,
							MemberMapper memberMapper, AuthenticatedUser authenticatedUser) {
		this.memberService = memberService;
		this.familyService = familyService;
		this.memberMapper = memberMapper;
		this.authenticatedUser = authenticatedUser;
	}
	
	@ApiOperation(value = "Get Member By Id")
	@GetMapping(value = "/{id}/with-children")
	public ResponseEntity<?> getMemberWithChildrenById(@PathVariable(name = "id") int id) throws Exception {
		MemberAccount account = memberService.getById(id);
		if(account == null){
			throw new ResourceNotFoundException("Resource Not Found");
		}
		MemberDTO memberDTO = memberMapper.toDto(account);
		return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
	}
	
	@ApiOperation(value = "Get Root Member")
	@GetMapping(value = "/root")
	public ResponseEntity<?> getRootByFid() {
		FamilyAccount familyAccount = authenticatedUser.get();
		MemberAccount account = memberService.getRootByFid(familyAccount.getId());
		if (account == null) {
			throw new ResourceNotFoundException("Resource Not found");
		}
		MemberDTO memberDTO = memberMapper.toDto(account);
		return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
	}
	
	@ApiOperation(value = "Save New Member")
	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody MemberInfoGeneralDTO memberInfoGeneralDTO,
								  @RequestParam(name = "parentId") int pid) throws Exception {
		FamilyAccount familyAccount = authenticatedUser.get();
		memberService.save(memberMapper.memberInfoGeneralToEntity(memberInfoGeneralDTO, familyService.getById(familyAccount.getId()), memberService.getById(pid)));
		return ResponseEntity.status(HttpStatus.CREATED).body(memberInfoGeneralDTO);
	}
	
	@ApiOperation(value = "Get member by Id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable(name = "id") int id) throws Exception {
		FamilyAccount familyAccount = authenticatedUser.get();
		MemberAccount memberAccount = memberService.getByFamilyIdAndId(id, familyAccount.getId());
		if (memberAccount == null) {
			throw new ResourceNotFoundException("Resource Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(memberMapper.entityToMemberInfoGeneralDTO(memberAccount));
	}
	
	@ApiOperation(value = "Update Member")
	@PutMapping(value = "/")
	public ResponseEntity<?> update(@Valid @RequestBody MemberInfoGeneralDTO memberInfoGeneralDTO,
									@RequestParam(name = "parentId") int pid) throws Exception {
		FamilyAccount familyAccount = authenticatedUser.get();
		memberService.update(memberMapper.memberInfoGeneralToEntity(memberInfoGeneralDTO, familyService.getById(familyAccount.getId()), memberService.getById(pid)));
		return ResponseEntity.status(HttpStatus.OK).body(memberInfoGeneralDTO);
	}

	@ApiOperation(value = "Get All Members")
	@GetMapping(value = "/")
	public ResponseEntity<?> getAll() {
		FamilyAccount familyAccount = authenticatedUser.get();
		List<MemberAccount> memberAccountList = memberService.getAllMembersByFid(familyAccount.getId());
		List<MemberDTO> memberAccounts = new ArrayList<>();
		for(MemberAccount account:memberAccountList){
			memberAccounts.add(memberMapper.toDto(account));
		}
		return ResponseEntity.status(HttpStatus.OK).body(memberAccounts);
	}
}
