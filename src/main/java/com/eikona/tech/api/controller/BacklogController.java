package com.eikona.tech.api.controller;

import java.net.URI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eikona.tech.domain.Backlog;
import com.eikona.tech.service.impl.BacklogServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class BacklogController {
	
	@Autowired
	BacklogServiceImpl backlogService;

	@GetMapping("/backlogs")
	public List<Backlog> getAllBacklogs(){
		return backlogService.findAll();
	}
	
	@GetMapping("/backlogs/id/{id}")
	public Backlog getBacklog(@PathVariable long id){
		return backlogService.find(id);
	}
	
	@PostMapping("/backlogs/id/{id}")
	public ResponseEntity<Void> createBacklog(@RequestBody Backlog backlog){
		Backlog createdBacklog = backlogService.save(backlog);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdBacklog.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/backlogs/id/{id}")
	public ResponseEntity<Backlog> updateBacklog(@PathVariable long id,
			@RequestBody Backlog backlog){
		Backlog updatedBacklog = backlogService.save(backlog);
		
		return new ResponseEntity<Backlog>(updatedBacklog, HttpStatus.OK) ;
	}
	
	@DeleteMapping("/backlogs/id/{id}")
	public ResponseEntity<Void> deleteBacklog(@PathVariable long id){
		backlogService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
