package com.eikona.tech.api.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eikona.tech.domain.Feedback;
import com.eikona.tech.dto.FeedbackDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.FeedbackService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
	
	@Autowired
	FeedbackService feedbackService;

	@GetMapping("/all")
	public List<Feedback> getAllFeedbacks(){
		return feedbackService.findAll();
	}
	
	@GetMapping("/id/{id}")
	public Feedback getFeedback(@PathVariable long id){
		return feedbackService.find(id);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Void> createFeedback(@RequestBody Feedback feedback){
		Feedback createdFeedback = feedbackService.save(feedback);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(createdFeedback.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<Feedback> updateFeedback(@PathVariable long id,
			@RequestBody Feedback feedback){
		Feedback updatedFeedback = feedbackService.save(feedback);
		
		return new ResponseEntity<Feedback>(updatedFeedback, HttpStatus.OK) ;
	}
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<Void> deleteFeedback(@PathVariable long id){
		feedbackService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public PaginatedDto<FeedbackDto> findPaginated(@RequestBody SearchRequestDto paginatedDto,
			Model model) {

		Page<Feedback> page = feedbackService.findPaginated(paginatedDto.getPageNo(), paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder());

		List<Feedback> feedbackList = page.getContent();
		List<FeedbackDto> feedbackDtoList = new ArrayList<>();
		for (Feedback feedback : feedbackList) {
			FeedbackDto feedbackDto = new FeedbackDto();
			
			feedbackDto.setBody(feedback.getBody());
			feedbackDto.setSubject(feedback.getSubject());
			feedbackDto.setUser(feedback.getUser());
			
			feedbackDtoList.add(feedbackDto);
		}

		List<Feedback> totalList = feedbackService.findAll();
		Page<Feedback> totalPage = new PageImpl<Feedback>(totalList);
		
		PaginatedDto<FeedbackDto> paginatedDtoList = new PaginatedDto<FeedbackDto>(feedbackDtoList,
				page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),"Success","");

		return paginatedDtoList;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody PaginatedDto<FeedbackDto> search(@RequestBody SearchRequestDto paginatedDto) {

		String message="";
		String messageType="";
		PaginatedDto<FeedbackDto> paginatedDtoList = null;
		try {
			
			Page<Feedback> page = feedbackService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
					paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto);
			
			List<Feedback> feedbackList = page.getContent();
			List<FeedbackDto> feedbackDtoList = new ArrayList<>();
			for (Feedback feedback : feedbackList) {
				FeedbackDto feedbackDto = new FeedbackDto();
				
				feedbackDto.setBody(feedback.getBody());
				feedbackDto.setSubject(feedback.getSubject());
				feedbackDto.setUser(feedback.getUser());
				
				feedbackDtoList.add(feedbackDto);
			}

			List<Feedback> totalList = feedbackService.findAll();
			Page<Feedback> totalPage = new PageImpl<Feedback>(totalList);
			message = "Success";
			messageType = "";
			paginatedDtoList = new PaginatedDto<FeedbackDto>(feedbackDtoList, page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements(),
					totalPage.getTotalElements(), message, messageType);
			
		}catch(Exception e) {
			System.out.println(e);
			message = "Failed";
			messageType = "";
			paginatedDtoList = new PaginatedDto<FeedbackDto>(message,messageType);
		}
		return paginatedDtoList;
	}
}
