package com.eikona.tech.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.service.impl.MultimediaLogServiceImpl;


@RestController
public class MultiMediaController {
	
	@Autowired
	MultimediaLogServiceImpl multimediaLogServiceImpl;

	@GetMapping("/sync/aws-database/{dateStr}")
	public String getAllBacklogs(@PathVariable String dateStr){
		multimediaLogServiceImpl.multimediaProcessingFromDirectory(dateStr);
		return "";
	}

}
