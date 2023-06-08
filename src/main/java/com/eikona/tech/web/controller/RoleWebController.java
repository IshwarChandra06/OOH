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

import com.eikona.tech.domain.Role;
import com.eikona.tech.service.RoleService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class RoleWebController {

	@Autowired
	private RoleService roleService;

	@GetMapping("/role")
	// @PreAuthorize("hasAnyRole('USER','ADMIN','MEDIA_OWNER')")
	public String list(Model model) {
		model.addAttribute(roleService.findAll());
		return "role/role_list";
	}

	@GetMapping("/role/new")
	// @PreAuthorize("hasAnyRole('ADMIN','MEDIA_OWNER')")
	public String newRole(Model model) {
		Role role = new Role();
		model.addAttribute("role", role);
		model.addAttribute("title", "New Role");
		model.addAttribute("button", "Save");
		return "role/role_new";
	}

	@PostMapping("/role/add")
	// @PreAuthorize("hasAnyRole('ADMIN','MEDIA_OWNER')")
	public String save(@ModelAttribute("role") Role role, @Valid Role entity, Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("title", "New Role");
			model.addAttribute("button", "Save");
			return "role/role_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			roleService.save(role);
			return "redirect:/role";

		}

	}

	@GetMapping("/role/edit/{id}")
	// @PreAuthorize("hasAnyRole('ADMIN','MEDIA_OWNER')")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Role role = roleService.find(id);
		model.addAttribute("role", role);
		model.addAttribute("title", "Update Role");
		model.addAttribute("button", "Update");
		return "role/role_new";
	}

	@GetMapping("/role/delete/{id}")
	// @PreAuthorize("hasAnyRole('MEDIA_OWNER')")
	public String delete(@PathVariable(value = "id") long id) {
		this.roleService.delete(id);
		return "redirect:/role";
	}

}
