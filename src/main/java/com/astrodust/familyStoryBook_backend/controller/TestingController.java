package com.astrodust.familyStoryBook_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {
	
	@GetMapping
	public String getMethod() {
		return "Hello there";
	}
}
