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

import com.eikona.tech.domain.PriceBreak;
import com.eikona.tech.service.PriceBreakService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class PriceBreakWebController {

	@Autowired
	private PriceBreakService priceBreakService;

	@GetMapping("/price/break")
	public String list(Model model) {
		model.addAttribute("PriceBreak", priceBreakService.findAll());
		return "pricebreak/pricebreak_list";
	}

	@GetMapping("/price/break/new")
	public String newPriceBreak(Model model) {
		PriceBreak pricebreak = new PriceBreak();
		model.addAttribute("pricebreak", pricebreak);
		model.addAttribute("title", "New Price Break");
		model.addAttribute("button", "Save");
		return "pricebreak/pricebreak_new";
	}

	@PostMapping("/price/break/add")
	public String save(@ModelAttribute("pricebreak") PriceBreak pricebreak, @Valid PriceBreak entity, Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("title", "New Price Break");
			model.addAttribute("button", "Save");
			return "pricebreak/pricebreak_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			priceBreakService.save(pricebreak);
			return "redirect:/price/break";

		}

	}

	@GetMapping("/price/break/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		PriceBreak pricebreak = priceBreakService.find(id);
		model.addAttribute("pricebreak", pricebreak);
		model.addAttribute("title", "Update Price Break");
		model.addAttribute("button", "Update");
		return "pricebreak/pricebreak_new";
	}

	@GetMapping("/price/break/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.priceBreakService.delete(id);
		return "redirect:/price/break";
	}
}
