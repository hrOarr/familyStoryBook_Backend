package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.EventDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.mapper.EventMapper;
import com.astrodust.familyStoryBook_backend.model.Event;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.EventService;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.utils.AuthenticatedUser;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
	private final EventService eventService;
	private final FamilyService familyService;
	private final EventMapper eventMapper;
	private final AuthenticatedUser authenticatedUser;

	@Autowired
	public EventController(EventService eventService, FamilyService familyService,
						   EventMapper eventMapper, AuthenticatedUser authenticatedUser) {
		this.eventService = eventService;
		this.familyService = familyService;
		this.eventMapper = eventMapper;
		this.authenticatedUser = authenticatedUser;
	}
	
	@ApiOperation(value = "Save New Event")
	@PostMapping(value = "/")
	public ResponseEntity<?> save(@Valid @RequestBody EventDTO eventDTO) throws Exception {
		FamilyAccount familyAccount = authenticatedUser.get();
		eventService.save(eventMapper.toEntity(eventDTO, familyAccount));
		return ResponseEntity.status(HttpStatus.CREATED).body(eventDTO);
	}
	
	@ApiOperation(value = "Get Event By Id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable int id) throws Exception {
		Event event = eventService.getById(id);
		if (event == null) {
			throw new ResourceNotFoundException("Resource Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(event);
	}
	
	@ApiOperation(value = "Update an Event")
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody EventDTO eventDTO,
									@PathVariable(name = "id") int id) throws Exception {
		eventDTO.setId(id);
		FamilyAccount familyAccount = authenticatedUser.get();
		Event event = eventMapper.toEntity(eventDTO, familyAccount);
		eventService.update(event);
		return ResponseEntity.status(HttpStatus.OK).body(eventDTO);
	}
	
	@ApiOperation(value = "Get Event By Id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable int id) throws Exception {
		Event event = eventService.getById(id);
		if (event == null) {
			throw new ResourceNotFoundException("Resource Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(event);
	}
	
	@ApiOperation(value = "Get All-Events By Family-Id")
	@GetMapping(value = "/")
	public ResponseEntity<?> getAllByFamilyId(@RequestParam(name = "familyId") int familyId) throws Exception {
		List<Event> events = eventService.getAllByFamilyId(familyId);
		return ResponseEntity.status(HttpStatus.OK).body(events);
	}
	
	@ApiOperation(value = "Delete Event By Id")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) throws Exception {
		Event event = eventService.getById(id);
		if (event == null) {
			throw new ResourceNotFoundException("Resource Not Found");
		}
		int count = eventService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("deleted = " + count + " row");
	}
}
