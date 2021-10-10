package com.astrodust.familyStoryBook_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.astrodust.familyStoryBook_backend.exception.GlobalExceptionHandler;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping(value = "/add/{familyId}")
	public ResponseEntity<?> save(@Valid @RequestBody EventDTO eventDTO, @PathVariable(name = "familyId") int id) throws Exception {
		try {
			eventService.save(converter.eventDTOtoEvent(eventDTO, familyService.getById(id)));
			return ResponseEntity.status(HttpStatus.CREATED).body(eventDTO);
		}
		catch (Exception e){
			logger.info("SoA:: exception from save() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Get Event for Edit")
	@GetMapping(value = "/edit/{id}")
	public ResponseEntity<?> edit(@PathVariable int id) throws Exception {
		try {
			Event event = eventService.getById(id);
			if (event == null) {
				throw new ResourceNotFoundException("Resource Not Found");
			}
			return ResponseEntity.ok(event);
		}
		catch (Exception e){
			logger.info("SoA:: exception from edit() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
	
	@ApiOperation(value = "Update an Event")
	@PutMapping(value = "/update/{familyId}")
	public ResponseEntity<?> update(@Valid @RequestBody EventDTO eventDTO, @PathVariable(name = "familyId") int fid) throws Exception {
		try {
			eventService.update(converter.eventDTOtoEvent(eventDTO, familyService.getById(fid)));
			return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
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
			return ResponseEntity.ok(event);
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
			List<Event> events = eventService.getAllByFamilyId(id);
			return ResponseEntity.ok(events);
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
			int count = eventService.delete(id);
			return ResponseEntity.ok("deleted = " + count + " row");
		}
		catch (Exception e){
			logger.info("SoA:: exception from delete() method---------------->", e);
			throw new Exception("Something went wrong. Please try again");
		}
	}
}
