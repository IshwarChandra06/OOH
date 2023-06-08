package com.eikona.tech.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eikona.tech.domain.Feedback;
import com.eikona.tech.service.FeedbackService;
import com.eikona.tech.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class FeedbackWebController {
	@Autowired
	private UserService userService;
	@Autowired
	private FeedbackService feedbackservice;

	@GetMapping("/feedback")
	public String list(Model model) {
		model.addAttribute("Feedback", feedbackservice.findAll());
		return "feedback/feedback_list";
	}

	@GetMapping("/feedback/new")
	public String newFeedBack(Model model) {

		model.addAttribute("listUser", userService.findAll());
		Feedback feedback = new Feedback();
		model.addAttribute("feedback", feedback);
		model.addAttribute("title", "New Feedback");
		model.addAttribute("button", "Save");
		return "feedback/feedback_new";
	}

	@PostMapping("/feedback/add")
	public String save(@ModelAttribute("feedback") Feedback feedback, @Valid Feedback entity, Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("listUser", userService.findAll());
			model.addAttribute("title", "New Feedback");
			model.addAttribute("button", "Save");
			return "feedback/feedback_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			feedbackservice.save(feedback);
			return "redirect:/feedback";

		}

	}

	@GetMapping("/feedback/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listUser", userService.findAll());
		Feedback feedback = feedbackservice.find(id);
		model.addAttribute("feedback", feedback);
		model.addAttribute("title", "Update Feedback");
		model.addAttribute("button", "update");
		return "feedback/feedback_new";
	}

	@GetMapping("/feedback/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.feedbackservice.delete(id);
		return "redirect:/feedback";
	}

}
