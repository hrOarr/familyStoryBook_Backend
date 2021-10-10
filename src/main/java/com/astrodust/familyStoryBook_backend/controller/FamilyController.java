package com.astrodust.familyStoryBook_backend.controller;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astrodust.familyStoryBook_backend.dto.FamilyRegisterDTO;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.service.FamilyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/family")
@Api(value = "FamilyRestAPI")
public class FamilyController {
	
	private static final Logger logger = LogManager.getLogger(FamilyController.class);
	private ModelMapper modelMapper;
	private FamilyService familyService;
	
	@Autowired
	public FamilyController(FamilyService familyService, ModelMapper modelMapper) {
		this.familyService = familyService;
		this.modelMapper = modelMapper;
	}
	
	@ApiOperation(value = "Get Single Family")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) throws Exception {
		try {
			FamilyAccount account = familyService.getById(id);
			if (account == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			return ResponseEntity.ok(account);
		}
		catch (Exception e){
			logger.info("SoA:: exception from getById() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Save New Family")
	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody FamilyRegisterDTO familyRegisterDTO) throws Exception {
		logger.info("Family Account save() method started->>>>>>");
		FamilyAccount account = modelMapper.map(familyRegisterDTO, FamilyAccount.class);
		account.setCreatedDate(LocalDateTime.now());
		try {
			familyService.save(account);
		} catch (Exception e) {
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
		return ResponseEntity.ok(familyRegisterDTO);
	}
}
