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

import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.service.ConstraintSingleService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ConstraintSingleWebController {
	
	@Autowired
	private ConstraintSingleService constraintSingleService;
	 

		
		 @GetMapping("/constraint/single") 
		 public String list(Model model) {
			 model.addAttribute("ConstraintSingle", constraintSingleService.findAll());
		 return "constraintsingle/constraintsingle_list"; 
		 }
		 
	@GetMapping("/constraint/single/new")
	public String newConstraintSingle(Model model) {
		ConstraintSingle constraintsingle = new ConstraintSingle();
		model.addAttribute("constraintsingle", constraintsingle);
		model.addAttribute("title","New Constraint Single");
		model.addAttribute("button","Save");
		return "constraintsingle/constraintsingle_new";
	}

	@PostMapping("/constraint/single/add")
	public String save(@ModelAttribute("constraintsingle") ConstraintSingle constraintsingle,@Valid ConstraintSingle entity,  Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("title","New Constraint Single");
			model.addAttribute("button","Save");
			return "constraintsingle/constraintsingle_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			constraintSingleService.save(constraintsingle);
			return "redirect:/constraint/single";

		}

	}

	@GetMapping("/constraint/single/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		ConstraintSingle constraintsingle = constraintSingleService.find(id);
		model.addAttribute("constraintsingle", constraintsingle);
		model.addAttribute("title","Update Constraint Single");
		model.addAttribute("button","Update");
		return "constraintsingle/constraintsingle_new";
	}

	@GetMapping("/constraint/single/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {

		this.constraintSingleService.delete(id);
		return "redirect:/constraint/single";
	}

}
