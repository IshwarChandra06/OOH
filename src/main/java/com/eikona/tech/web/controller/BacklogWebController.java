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

import com.eikona.tech.domain.Backlog;
import com.eikona.tech.service.BacklogService;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class BacklogWebController {

	@Autowired
	private BacklogService backlogservice;

	@Autowired
	private MediaSiteService mediasiteService;

	@Autowired
	private UserService userService;

	@GetMapping("/back/log")
	public String list(Model model) {
		model.addAttribute("BackLog", backlogservice.findAll());
		return "backlog/backlog_list";
	}

	@GetMapping("/back/log/new")
	public String newBackLog(Model model) {

		model.addAttribute("listMediaSite", mediasiteService.findAll());
		model.addAttribute("listUser", userService.findAll());
		Backlog backlog = new Backlog();
		model.addAttribute("backlog", backlog);
		model.addAttribute("title", "New Back Log");
		model.addAttribute("button", "Save");
		return "backlog/backlog_new";
	}

	@PostMapping("/back/log/add")
	public String save(@ModelAttribute("backlog") Backlog backlog, @Valid Backlog entity, Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("listMediaSite", mediasiteService.findAll());
			model.addAttribute("listUser", userService.findAll());
			model.addAttribute("title", "New Back Log");
			model.addAttribute("button", "Save");
			return "backlog/backlog_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			backlogservice.save(backlog);
			return "redirect:/back/log";

		}

	}

	@GetMapping("/back/log/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listMediaSite", mediasiteService.findAll());
		model.addAttribute("listUser", userService.findAll());
		Backlog backlog = backlogservice.find(id);
		model.addAttribute("backlog", backlog);
		model.addAttribute("title", "Update Back Log");
		model.addAttribute("button", "Update");
		return "backlog/backlog_new";
	}

	@GetMapping("/back/log/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {

		this.backlogservice.delete(id);
		return "redirect:/back/log";
	}
}
