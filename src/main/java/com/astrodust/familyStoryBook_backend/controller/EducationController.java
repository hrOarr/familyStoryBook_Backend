package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertEducationDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateEducationDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.model.MemberEducation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.service.EducationService;
import com.astrodust.familyStoryBook_backend.service.MemberService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/member/education")
public class EducationController {
	
	private static final Logger logger = LogManager.getLogger(EducationController.class);
	private EducationService educationService;
	private MemberService memberService;
	private Converter converter;
	
	@Autowired
	public EducationController(EducationService educationService, MemberService memberService, Converter converter) {
		this.educationService = educationService;
		this.memberService = memberService;
		this.converter = converter;
	}
	
	@ApiOperation(value = "Get Education-list by Member-Id")
	@GetMapping(value = "/getByMemberId/{memberId}")
	public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int mid) throws Exception {
		try {
			return ResponseEntity.ok(educationService.getByMemberId(mid));
		} catch (Exception e) {
			logger.info("SoA:: exception from getByMemberId() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Save Education-list")
	@PostMapping(value = "/save/{memberId}")
	public ResponseEntity<?> save(@Valid @RequestBody MemberInsertEducationDTO memberInsertEducationDTO, @PathVariable(name = "memberId") int mid) throws Exception {
		try {
			educationService.save(converter.memberInsertEducationDTOtoMemberEducation(memberInsertEducationDTO, memberService.getById(mid)));
			return ResponseEntity.ok("Saved!!!");
		} catch (Exception e) {
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}

	@ApiOperation(value = "Get A Education for update")
	@GetMapping(value = "/edit/{id}")
	public ResponseEntity<?> edit(@PathVariable int id) throws Exception {
		try {
			MemberEducation memberEducation = educationService.getById(id);
			if (memberEducation == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			return ResponseEntity.ok(memberEducation);
		}
		catch (ResourceNotFoundException e){
			throw new ResourceNotFoundException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from edit() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}

	@ApiOperation(value = "Update Education")
	@PutMapping(value = "/update/{memberId}")
	public ResponseEntity<?> update(@Valid @RequestBody MemberUpdateEducationDTO memberUpdateEducationDTO,
									@PathVariable(name = "memberId") int id) throws Exception {
		try {
			educationService.updateOne(converter.memberUpdateEducationDTOtoMemberEducation(memberUpdateEducationDTO, memberService.getById(id)));
			return ResponseEntity.status(HttpStatus.OK).body(educationService.getById(memberUpdateEducationDTO.getId()));
		}
		catch (Exception e){
			logger.info("SoA:: exception from update() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "delete education by id")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) throws Exception {
		try {
			int cnt = educationService.delete(id);
			logger.info(cnt + " ----------????????????");
			return ResponseEntity.ok(cnt + " row(s) are deleted");
		} catch (Exception e) {
			throw new Exception("Something went wrong. Please try again.");
		}
	}
	
}
