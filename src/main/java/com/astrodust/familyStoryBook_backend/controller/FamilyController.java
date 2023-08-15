package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.FamilyRegisterDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.mapper.FamilyMapper;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/families")
@Api(value = "FamilyRestAPI")
public class FamilyController {
	private final FamilyService familyService;
	private final FamilyMapper familyMapper;
	
	@Autowired
	public FamilyController(FamilyService familyService, FamilyMapper familyMapper) {
		this.familyService = familyService;
		this.familyMapper = familyMapper;
	}
	
	@ApiOperation(value = "Get Family By Id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) throws Exception {
		FamilyAccount account = familyService.getById(id);
		if (account == null) {
			throw new ResourceNotFoundException("Resource Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(account);
	}
	
	@ApiOperation(value = "Save New Family")
	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody FamilyRegisterDTO familyRegisterDTO) throws Exception {
		FamilyAccount familyAccount = familyMapper.toEntity(familyRegisterDTO);
		familyService.save(familyAccount);
		return ResponseEntity.ok(familyAccount);
	}

	@ApiOperation(value = "Delete Family By Id")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") int id){
		int count = familyService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(count + " row(s) are deleted");
	}
}
