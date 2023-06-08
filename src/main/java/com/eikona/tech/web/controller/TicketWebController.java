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

import com.eikona.tech.domain.Ticket;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.service.TicketService;
import com.eikona.tech.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class TicketWebController {
	@Autowired
	private UserService userService;
	@Autowired
	private MediaSiteService mediaSiteService;
	@Autowired
	private TicketService ticketService;

	@GetMapping("/ticket")
	public String list(Model model) {
		model.addAttribute("Ticket", ticketService.findAll());
		return "ticket/ticket_list";
	}

	@GetMapping("/ticket/new")
	public String newTicket(Model model) {
		model.addAttribute("listMediaSite", mediaSiteService.findAll());
		model.addAttribute("listUser", userService.findAll());
		Ticket ticket = new Ticket();
		model.addAttribute("ticket", ticket);
		model.addAttribute("title", "New Ticket");
		model.addAttribute("button", "Save");
		return "ticket/ticket_new";
	}

	@PostMapping("/ticket/add")
	public String save(@ModelAttribute("imagelog") Ticket ticket, @Valid Ticket entity, Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("listMediaSite", mediaSiteService.findAll());
			model.addAttribute("listUser", userService.findAll());
			model.addAttribute("title", "New Ticket");
			model.addAttribute("button", "Save");
			return "ticket/ticket_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			ticketService.save(ticket);
			return "redirect:/ticket";

		}

	}

	@GetMapping("/ticket/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listMediaSite", mediaSiteService.findAll());
		model.addAttribute("listUser", userService.findAll());
		Ticket ticket = ticketService.find(id);
		model.addAttribute("ticket", ticket);
		model.addAttribute("title", "Update Ticket");
		model.addAttribute("button", "Update");
		return "ticket/ticket_new";
	}

	@GetMapping("/ticket/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.ticketService.delete(id);
		return "redirect:/ticket";
	}

}
