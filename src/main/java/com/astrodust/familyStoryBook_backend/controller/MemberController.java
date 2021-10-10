package com.astrodust.familyStoryBook_backend.controller;

import javax.validation.Valid;

import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astrodust.familyStoryBook_backend.dto.MemberDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberInfoGeneralDTO;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.service.FamilyService;
import com.astrodust.familyStoryBook_backend.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable int id) throws Exception {
		try {
			logger.info("MemberController getById() method init->>>>>>>>>>");
			MemberAccount account = memberService.getById(id);
			MemberDTO memberDTO = converter.memberToMemDTO(account);
			return ResponseEntity.ok(memberDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from getById() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get Root Member")
	@GetMapping(value = "/root/{familyId}")
	public ResponseEntity<?> getRootByFid(@PathVariable(name = "familyId") int f_id) throws Exception {
		try {
			logger.info("MemberController getRootByFid() method init->>>>>>>>>>");
			MemberAccount account = memberService.getRootByFid(f_id);
			if (account == null) {
				throw new ResourceNotFoundException("Resource Not found");
			}
			MemberDTO memberDTO = converter.memberToMemDTO(account);
			return ResponseEntity.ok(memberDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from getRootByFid() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Save New Member")
	@PostMapping(value = "/add/{familyId}/{parentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody MemberInfoGeneralDTO memberInfoGeneralDTO, 
			@PathVariable(name = "familyId") int fid, @PathVariable(name = "parentId") int pid) throws Exception {
		try {
			logger.info("MemberController save() method init->>>>>>");
			memberService.save(converter.memberInfoGeneralToMem(memberInfoGeneralDTO, familyService.getById(fid), memberService.getById(pid)));
			return ResponseEntity.status(HttpStatus.CREATED).body(memberInfoGeneralDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get member for update")
	@GetMapping(value = "edit/{id}/{familyId}")
	public ResponseEntity<?> edit(@PathVariable(name = "id") int id, @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			logger.info("MemberController edit() method init->>>>>>>>>>");
			MemberAccount memberAccount = memberService.getByFamilyIdAndId(id, fid);
			if (memberAccount == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			return ResponseEntity.ok(converter.memberToMemberInfoGeneralDTO(memberAccount));
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
			memberService.update(converter.memberInfoGeneralToMem(memberInfoGeneralDTO, familyService.getById(fid), memberService.getById(pid)));
			return ResponseEntity.ok(memberInfoGeneralDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from update() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
}
