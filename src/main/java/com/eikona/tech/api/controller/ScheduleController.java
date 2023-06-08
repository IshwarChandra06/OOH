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

import com.eikona.tech.domain.Schedule;
import com.eikona.tech.service.impl.ScheduleServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class ScheduleController {
	
	@Autowired
	ScheduleServiceImpl scheduleService;

	@GetMapping("/schedules")
	public List<Schedule> getAllSchedules(){
		return scheduleService.findAll();
	}
	
	@GetMapping("/schedules/id/{id}")
	public Schedule getSchedule(@PathVariable long id){
		return scheduleService.find(id);
	}
	
	@PostMapping("/schedules/id/{id}")
	public ResponseEntity<Void> createSchedule(@RequestBody Schedule schedule){
		Schedule createdSchedule = scheduleService.save(schedule);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdSchedule.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/schedules/id/{id}")
	public ResponseEntity<Schedule> updateSchedule(@PathVariable long id,
			@RequestBody Schedule schedule){
		Schedule updatedSchedule = scheduleService.save(schedule);
		
		return new ResponseEntity<Schedule>(updatedSchedule, HttpStatus.OK) ;
	}
	
	@DeleteMapping("/schedules/id/{id}")
	public ResponseEntity<Void> deleteSchedule(@PathVariable long id){
		scheduleService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
