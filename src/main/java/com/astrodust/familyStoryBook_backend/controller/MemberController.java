package com.astrodust.familyStoryBook_backend.controller;

import javax.validation.Valid;

import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.astrodust.familyStoryBook_backend.dto.MemberDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberInfoGeneralDTO;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/family/member")
@Api(value = "MemberRestAPI")
public class MemberController {
	private static final Logger logger = LogManager.getLogger(MemberController.class);
	private MemberService memberService;
	private FamilyService familyService;
	private Converter converter;
	
	@Autowired
	public MemberController(MemberService memberService, FamilyService familyService, Converter converter) {
		this.memberService = memberService;
		this.familyService = familyService;
		this.converter = converter;
	}
	
	@ApiOperation(value = "Get Single Member")
	@GetMapping(value = "/memberId/{id}/familyId/{familyId}")
	public ResponseEntity<?> getById(@PathVariable(name = "id") int id, @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			logger.info("MemberController getById() method init->>>>>>>>>>");
			MemberAccount account = memberService.getById(id);
			if(account == null){
				throw new ResourceNotFoundException("Resource Not Found");
			}
			FamilyAccount familyAccount = familyService.getById(fid);
			// authorization check
			IsAuthorized(familyAccount);
			MemberDTO memberDTO = converter.memberToMemDTO(account);
			return ResponseEntity.ok(memberDTO);
		}
		catch (ResourceNotFoundException e){
			throw new ResourceNotFoundException(e.getLocalizedMessage());
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from getById() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get Root Member")
	@GetMapping(value = "/root/{familyId}")
	public ResponseEntity<?> getRootByFid(@PathVariable(name = "familyId") int fid) throws Exception {
		try {
			logger.info("MemberController getRootByFid() method init->>>>>>>>>>");
			MemberAccount account = memberService.getRootByFid(fid);
			if (account == null) {
				throw new ResourceNotFoundException("Resource Not found");
			}
			// authorization check
			FamilyAccount familyAccount = familyService.getById(fid);
			IsAuthorized(familyAccount);
			MemberDTO memberDTO = converter.memberToMemDTO(account);
			return ResponseEntity.ok(memberDTO);
		}
		catch (ResourceNotFoundException e){
			throw new ResourceNotFoundException(e.getLocalizedMessage());
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from getRootByFid() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Save New Member")
	@PostMapping(value = "/add/familyId/{familyId}/parentId/{parentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody MemberInfoGeneralDTO memberInfoGeneralDTO, 
			@PathVariable(name = "familyId") int fid, @PathVariable(name = "parentId") int pid) throws Exception {
		try {
			logger.info("MemberController save() method init->>>>>>");
			// authorization check
			FamilyAccount familyAccount = familyService.getById(fid);
			IsAuthorized(familyAccount);
			memberService.save(converter.memberInfoGeneralToMem(memberInfoGeneralDTO, familyService.getById(fid), memberService.getById(pid)));
			return ResponseEntity.status(HttpStatus.CREATED).body(memberInfoGeneralDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get member for update")
	@GetMapping(value = "edit/memberId/{id}/familyId/{familyId}")
	public ResponseEntity<?> edit(@PathVariable(name = "id") int id, @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			logger.info("MemberController edit() method init->>>>>>>>>>");
			MemberAccount memberAccount = memberService.getByFamilyIdAndId(id, fid);
			if (memberAccount == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			// authorization check
			FamilyAccount familyAccount = familyService.getById(fid);
			IsAuthorized(familyAccount);
			return ResponseEntity.ok(converter.memberToMemberInfoGeneralDTO(memberAccount));
		}
		catch (ResourceNotFoundException e){
			throw new ResourceNotFoundException(e.getLocalizedMessage());
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from edit() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Update Member")
	@PutMapping(value = "/update/{familyId}/{parentId}")
	public ResponseEntity<?> update(@Valid @RequestBody MemberInfoGeneralDTO memberInfoGeneralDTO,
			@PathVariable(name = "familyId") int fid, @PathVariable(name = "parentId") int pid) throws Exception {
		try {
			logger.info("MemberController update() method init->>>>>>");
			// authorization check
			FamilyAccount familyAccount = familyService.getById(fid);
			IsAuthorized(familyAccount);
			memberService.update(converter.memberInfoGeneralToMem(memberInfoGeneralDTO, familyService.getById(fid), memberService.getById(pid)));
			return ResponseEntity.ok(memberInfoGeneralDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from update() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}

	@ApiOperation(value = "Get All Members")
	@GetMapping(value = "/getAll/familyId/{familyId}")
	public ResponseEntity<?> getAllMembers(@PathVariable(name = "familyId") int fid) throws Exception {
		try {
			// authorization check
			FamilyAccount familyAccount = familyService.getById(fid);
			IsAuthorized(familyAccount);
			List<MemberAccount> memberAccountList = memberService.getAllMembersByFid(fid);
			List<MemberDTO> memberAccounts = new ArrayList<>();
			for(MemberAccount account:memberAccountList){
				memberAccounts.add(converter.memberToMemDTO(account));
			}
			return ResponseEntity.ok(memberAccounts);
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException("You are not authorized to access");
		}
		catch (Exception e){
			throw new Exception("Something went wrong. Please try again");
		}
	}

	public void IsAuthorized(FamilyAccount familyAccount){
		String currentUsername = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails){
			currentUsername = (((UserDetails) principal).getUsername());
		}
		else{
			currentUsername = principal.toString();
		}
		if(familyAccount==null){
			return;
		}
		String email = familyAccount.getEmail();
		if(email.equals(currentUsername)==false){
			throw new AccessDeniedException("You are not authorized to access");
		}
	}
}
