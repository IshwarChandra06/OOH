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

import com.eikona.tech.domain.Schedule;
import com.eikona.tech.service.ScheduleService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ScheduleWebController {

	@Autowired
	private ScheduleService scheduleService;

	@GetMapping("/scheduler")
	public String list(Model model) {
		model.addAttribute("Schedule", scheduleService.findAll());
		return "schedule/schedule_list";
	}

	@GetMapping("/scheduler/new")
	public String newSchedule(Model model) {
		Schedule schedule = new Schedule();
		model.addAttribute("schedule", schedule);
		model.addAttribute("title", "New Schedule");
		model.addAttribute("button", "Save");
		return "schedule/schedule_new";
	}

	@PostMapping("/scheduler/add")
	public String save(@ModelAttribute("schedule") Schedule schedule, @Valid Schedule entity, Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("title", "New Schedule");
			model.addAttribute("button", "Save");
			return "schedule/schedule_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			scheduleService.save(schedule);
			return "redirect:/scheduler";

		}

	}

	@GetMapping("/scheduler/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Schedule schedule = scheduleService.find(id);
		model.addAttribute("schedule", schedule);
		model.addAttribute("title", "Update Schedule");
		model.addAttribute("button", "Update");
		return "schedule/schedule_new";
	}

	@GetMapping("/scheduler/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.scheduleService.delete(id);
		return "redirect:/scheduler";
	}
}
