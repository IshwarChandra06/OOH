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

import com.eikona.tech.domain.PriceBreak;
import com.eikona.tech.service.impl.PriceBreakServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class PriceBreakController {
	
	@Autowired
	PriceBreakServiceImpl priceBreakService;

	@GetMapping("/priceBreaks")
	public List<PriceBreak> getAllPriceBreaks(){
		return priceBreakService.findAll();
	}
	
	@GetMapping("/priceBreaks/id/{id}")
	public PriceBreak getPriceBreak(@PathVariable long id){
		return priceBreakService.find(id);
	}
	
	@PostMapping("/priceBreaks/id/{id}")
	public ResponseEntity<Void> createPriceBreak(@RequestBody PriceBreak priceBreak){
		PriceBreak createdPriceBreak = priceBreakService.save(priceBreak);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdPriceBreak.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/priceBreaks/id/{id}")
	public ResponseEntity<PriceBreak> updatePriceBreak(@PathVariable long id,
			@RequestBody PriceBreak priceBreak){
		PriceBreak updatedPriceBreak = priceBreakService.save(priceBreak);
		
		return new ResponseEntity<PriceBreak>(updatedPriceBreak, HttpStatus.OK) ;
	}
	
	@DeleteMapping("/priceBreaks/id/{id}")
	public ResponseEntity<Void> deletePriceBreak(@PathVariable long id){
		priceBreakService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
