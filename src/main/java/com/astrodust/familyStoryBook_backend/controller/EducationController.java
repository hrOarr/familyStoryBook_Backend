package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertEducationDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateEducationDTO;
import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.mapper.EducationMapper;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberEducation;
import com.astrodust.familyStoryBook_backend.service.interfaces.EducationService;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/{memberId}/educations")
public class EducationController {
	private final EducationService educationService;
	private final MemberService memberService;
	private final FamilyService familyService;
	private final EducationMapper educationMapper;

	@Autowired
	public EducationController(EducationService educationService, FamilyService familyService, MemberService memberService, EducationMapper educationMapper) {
		this.educationService = educationService;
		this.familyService = familyService;
		this.memberService = memberService;
		this.educationMapper = educationMapper;
	}
	
	@ApiOperation(value = "Get Education-list by Member-Id")
	@GetMapping(value = "/")
	public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int memberId) throws Exception {
		return ResponseEntity.ok(educationService.getByMemberId(memberId));
	}
	
	@ApiOperation(value = "Save Education-list")
	@PostMapping(value = "/")
	public ResponseEntity<?> save(@Valid @RequestBody List<MemberInsertEducationDTO> memberInsertEducationDTO,
								  @PathVariable(name = "memberId") int memberId) throws Exception {
		List<MemberEducation> memberEducations = educationMapper.memberInsertEducationDTOtoEntity(memberInsertEducationDTO, memberService.getById(memberId));
		educationService.save(memberEducations);
		return ResponseEntity.status(HttpStatus.CREATED).body("Successful");
	}

	@ApiOperation(value = "Get A Education for update")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable(name = "id") int id,
								  @PathVariable(name = "memberId") int memberId) {
		MemberEducation memberEducation = educationService.getById(id);
		return ResponseEntity.status(HttpStatus.OK).body(memberEducation);
	}

	@ApiOperation(value = "Update Education")
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody MemberUpdateEducationDTO memberUpdateEducationDTO,
									@PathVariable(name = "id") int id,
									@PathVariable(name = "memberId") int memberId) throws Exception {
		memberUpdateEducationDTO.setId(id);
		educationService.updateOne(educationMapper.memberUpdateEducationDTOtoEntity(memberUpdateEducationDTO, memberService.getById(memberId)));
		return ResponseEntity.status(HttpStatus.OK).body(educationService.getById(memberUpdateEducationDTO.getId()));
	}
	
	@ApiOperation(value = "Delete education by id")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") int id,
									@PathVariable(name = "memberId") int memberId) throws Exception {
		int count = educationService.delete(id);
		return ResponseEntity.ok(count + " row(s) are deleted");
	}

	public void IsAuthorized(int fid,int eid,int mid){
		String currentUsername = "";
		FamilyAccount familyAccount = familyService.getById(fid);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails){
			currentUsername = (((UserDetails) principal).getUsername());
		}
		else{
			currentUsername = principal.toString();
		}
		if(familyAccount==null || currentUsername.equals(familyAccount.getEmail())==false) {
			throw new AccessDeniedException("You are not authorized to access");
		}

		if(eid!=0){
			MemberEducation memberEducation = educationService.getById(eid);
			if(memberEducation == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			if(memberEducation.getMemberAccount().getId()!=mid){
				throw new AccessDeniedException("You are not authorized to access");
			}
		}
	}
	
}
