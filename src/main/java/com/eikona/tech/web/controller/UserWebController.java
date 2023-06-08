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

import com.eikona.tech.domain.User;
import com.eikona.tech.service.OrganizationService;
import com.eikona.tech.service.RoleService;
import com.eikona.tech.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class UserWebController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@GetMapping("/user")
	public String list(Model model) {
		model.addAttribute("User", userService.findAll());
		return "user/user_list";
	}

	@GetMapping("/user/new")
	public String newUser(Model model) {
		model.addAttribute("listRole", roleService.findAll());
		model.addAttribute("listOrganization", organizationService.findAll());
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("title", "New User");
		model.addAttribute("button", "Save");
		return "user/user_new";
	}

	@PostMapping("/user/add")
	public String save(@ModelAttribute("user") User user, @Valid User entity, Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("listRole", roleService.findAll());
			model.addAttribute("listOrganization", organizationService.findAll());
			model.addAttribute("title", "New User");
			model.addAttribute("button", "Save");
			return "user/user_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			userService.save(user);
			return "redirect:/user";
		}
	}

	@GetMapping("/user/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listRole", roleService.findAll());
		model.addAttribute("listOrganization", organizationService.findAll());
		User user = userService.find(id);
		model.addAttribute("user", user);
		model.addAttribute("title", "Update User");
		model.addAttribute("button", "Update");
		return "user/user_new";
	}

	@GetMapping("user/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.userService.delete(id);
		return "redirect:/user";
	}
}
