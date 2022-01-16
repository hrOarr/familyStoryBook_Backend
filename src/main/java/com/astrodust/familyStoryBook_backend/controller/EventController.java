package com.astrodust.familyStoryBook_backend.controller;

import java.util.List;

import javax.validation.Valid;

import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.astrodust.familyStoryBook_backend.dto.EventDTO;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.Event;
import com.astrodust.familyStoryBook_backend.service.EventService;
import com.astrodust.familyStoryBook_backend.service.FamilyService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/family/event")
public class EventController {
	private static final Logger logger = LogManager.getLogger(EventController.class);
	
	private EventService eventService;
	private FamilyService familyService;
	private Converter converter;
	
	@Autowired
	public EventController(EventService eventService, FamilyService familyService, Converter converter) {
		this.eventService = eventService;
		this.familyService = familyService;
		this.converter = converter;
	}
	
	@ApiOperation(value = "Save New Event")
	@PostMapping(value = "/add/familyId/{familyId}")
	public ResponseEntity<?> save(@Valid @RequestBody EventDTO eventDTO, @PathVariable(name = "familyId") int id) throws Exception {
		try {
			logger.info("SoA:: " + eventDTO);
			eventService.save(converter.eventDTOtoEvent(eventDTO, familyService.getById(id)));
			return ResponseEntity.status(HttpStatus.CREATED).body(eventDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get Event for Edit")
	@GetMapping(value = "/edit/eventId/{id}")
	public ResponseEntity<?> edit(@PathVariable int id) throws Exception {
		try {
			Event event = eventService.getById(id);
			if (event == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			// authorization check
			IsAuthorized(event);
			return ResponseEntity.ok(event);
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
	
	@ApiOperation(value = "Update an Event")
	@PutMapping(value = "/update/familyId/{familyId}/eventId/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody EventDTO eventDTO, @PathVariable(name = "familyId") int fid,
									@PathVariable(name = "id") int id) throws Exception {
		try {
			eventDTO.setId(id);
			Event event = converter.eventDTOtoEvent(eventDTO, familyService.getById(fid));
			// authorization check
			IsAuthorized(event);
			eventService.update(event);
			return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from update() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get Single Event")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable int id) throws Exception {
		try {
			Event event = eventService.getById(id);
			if (event == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			// authorization check
			IsAuthorized(event);
			return ResponseEntity.ok(event);
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
	
	@ApiOperation(value = "Get All-Events")
	@GetMapping(value = "/allEvents")
	public ResponseEntity<?> getAllEvents() throws Exception {
		try {
			List<Event> events = eventService.getAllEvents();
			return ResponseEntity.ok(events);
		}
		catch (Exception e){
			logger.info("SoA:: exception from getAllEvents() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get All-Events By Family-Id")
	@GetMapping(value = "/allEventsBy/{familyId}")
	public ResponseEntity<?> getAllByFamilyId(@PathVariable(name = "familyId") int id) throws Exception {
		try {
			// authorization check
			String currentUsername = "";
			FamilyAccount familyAccount = familyService.getById(id);
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal instanceof UserDetails){
				currentUsername = (((UserDetails) principal).getUsername());
			}
			else{
				currentUsername = principal.toString();
			}
			logger.info("SoA :: " + currentUsername);
			if(familyAccount==null || currentUsername.equals(familyAccount.getEmail())==false){
				throw new AccessDeniedException("You are not authorized to access");
			}
			List<Event> events = eventService.getAllByFamilyId(id);
			return ResponseEntity.ok(events);
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from getAllByFamilyId() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Delete Single Event")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) throws Exception {
		try {
			Event event = eventService.getById(id);
			if (event == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			// authorization check
			IsAuthorized(event);
			int count = eventService.delete(id);
			return ResponseEntity.ok("deleted = " + count + " row");
		}
		catch (ResourceNotFoundException e){
			throw new ResourceNotFoundException(e.getLocalizedMessage());
		}
		catch (AccessDeniedException e){
			throw new AccessDeniedException(e.getLocalizedMessage());
		}
		catch (Exception e){
			logger.info("SoA:: exception from delete() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}

	public void IsAuthorized(Event event){
		String currentUsername = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails){
			currentUsername = (((UserDetails) principal).getUsername());
		}
		else{
			currentUsername = principal.toString();
		}
		String email = event.getFamilyAccount().getEmail();
		logger.info("SoA:: event-->" + email);
		if(email.equals(currentUsername)==false){
			throw new AccessDeniedException("You are not authorized to access");
		}
	}
}
