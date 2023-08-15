package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertEducationDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateEducationDTO;
import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberEducation;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.service.interfaces.EducationService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member/education")
public class EducationController {
	
	private static final Logger logger = LogManager.getLogger(EducationController.class);
	private EducationService educationService;
	private MemberService memberService;
	private FamilyService familyService;
	private Converter converter;
	
	@Autowired
	public EducationController(EducationService educationService, FamilyService familyService, MemberService memberService, Converter converter) {
		this.educationService = educationService;
		this.familyService = familyService;
		this.memberService = memberService;
		this.converter = converter;
	}
	
	@ApiOperation(value = "Get Education-list by Member-Id")
	@GetMapping(value = "/getAll/memberId/{memberId}/familyId/{familyId}")
	public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			// authorization check
			IsAuthorized(fid, 0,0);
			return ResponseEntity.ok(educationService.getByMemberId(mid));
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e) {
			logger.info("SoA:: exception from getByMemberId() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Save Education-list")
	@PostMapping(value = "/save/memberId/{memberId}/familyId/{familyId}")
	public ResponseEntity<?> save(@Valid @RequestBody List<MemberInsertEducationDTO> memberInsertEducationDTO,
								  @PathVariable(name = "memberId") int mid,
								  @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			IsAuthorized(fid, 0, 0);
			educationService.save(converter.memberInsertEducationDTOtoMemberEducation(memberInsertEducationDTO, memberService.getById(mid)));
			return ResponseEntity.ok("Saved!!!");
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e) {
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}

	@ApiOperation(value = "Get A Education for update")
	@GetMapping(value = "/edit/id/{id}/memberId/{memberId}/familyId/{familyId}")
	public ResponseEntity<?> edit(@PathVariable(name = "id") int id,
								  @PathVariable(name = "memberId") int mid,
								  @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			// authorization check
			IsAuthorized(fid, id, mid);
			MemberEducation memberEducation = educationService.getById(id);
			return ResponseEntity.ok(memberEducation);
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

	@ApiOperation(value = "Update Education")
	@PutMapping(value = "/update/id/{id}/memberId/{memberId}/familyId/{familyId}")
	public ResponseEntity<?> update(@Valid @RequestBody MemberUpdateEducationDTO memberUpdateEducationDTO,
									@PathVariable(name = "id") int id,
									@PathVariable(name = "memberId") int mid,
									@PathVariable(name = "familyId") int fid) throws Exception {
		try {
			// authorization check
			IsAuthorized(fid, id, mid);
			memberUpdateEducationDTO.setId(id);
			educationService.updateOne(converter.memberUpdateEducationDTOtoMemberEducation(memberUpdateEducationDTO, memberService.getById(mid)));
			return ResponseEntity.status(HttpStatus.OK).body(educationService.getById(memberUpdateEducationDTO.getId()));
		}
		catch (ResourceNotFoundException e){
			throw new ResourceNotFoundException(e.getLocalizedMessage());
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from update() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "delete education by id")
	@DeleteMapping(value = "/delete/id/{id}/memberId/{memberId}/familyId/{familyId}")
	public ResponseEntity<?> delete(@PathVariable(name = "id") int id,
									@PathVariable(name = "memberId") int mid,
									@PathVariable(name = "familyId") int fid) throws Exception {
		try {
			// authorization check
			IsAuthorized(fid, id, mid);
			int cnt = educationService.delete(id);
			logger.info(cnt + " ----------????????????");
			return ResponseEntity.ok(cnt + " row(s) are deleted");
		} catch (Exception e) {
			throw new Exception("Something went wrong. Please try again.");
		}
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
		logger.info("SoA :: " + currentUsername);
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
