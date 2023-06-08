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

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.service.ConstraintRangeService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ConstraintRangeWebController {
	
	 @Autowired
		private ConstraintRangeService constraintRangeService;
		 

			
			 @GetMapping("/constraint/range") 
			 public String list(Model model) {
				 model.addAttribute("ConstraintRange", constraintRangeService.findAll());
			 return "constraintrange/constraintrange_list"; 
			 }
			 
		@GetMapping("/constraint/range/new")
		public String newConstraintRange(Model model) {
			ConstraintRange constraintrange = new ConstraintRange();
			model.addAttribute("constraintrange", constraintrange);
			model.addAttribute("title","New Constraint Range");
			model.addAttribute("button","Save");
			return "constraintrange/constraintrange_new";
		}

		@PostMapping("/constraint/range/add")
		public String save(@ModelAttribute("constraintrange") ConstraintRange constraintrange,@Valid ConstraintRange entity, Errors errors,
				Model model) {
			if (errors.hasErrors()) {
				model.addAttribute("title","New Constraint Range");
				model.addAttribute("button","Save");
				return "constraintrange/constraintrange_new";
			} else {
				model.addAttribute("message", "Add Successfully");
				constraintRangeService.save(constraintrange);
				return "redirect:/constraint/range";

			}

		}

		@GetMapping("/constraint/range/edit/{id}")
		public String edit(@PathVariable(value = "id") long id, Model model) {
			ConstraintRange constraintrange = constraintRangeService.find(id);
			model.addAttribute("constraintrange", constraintrange);
			model.addAttribute("title","Update Constraint Range");
			model.addAttribute("button","Update");
			return "constraintrange/constraintrange_new";
		}

		@GetMapping("/constraint/range/delete/{id}")
		public String delete(@PathVariable(value = "id") long id) {

			this.constraintRangeService.delete(id);
			return "redirect:/constraint/range";
		}

}
