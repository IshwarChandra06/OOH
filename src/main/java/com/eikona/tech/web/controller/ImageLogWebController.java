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

import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.service.MultimediaLogService;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class ImageLogWebController {

	@Autowired
	private UserService userService;
	@Autowired
	private MediaSiteService mediaSiteService;
	@Autowired
	private MultimediaLogService imageLogService;

	@GetMapping("/image/log")
	public String list(Model model) {
		model.addAttribute("ImageLog", imageLogService.findAll());
		return "imagelog/imagelog_list";
	}

	@GetMapping("/image/log/new")
	public String newImageLog(Model model) {
		model.addAttribute("listMediaSite", mediaSiteService.findAll());
		model.addAttribute("listUser", userService.findAll());
		MultimediaLog imagelog = new MultimediaLog();
		model.addAttribute("imagelog", imagelog);
		model.addAttribute("title", "New Image Log");
		model.addAttribute("button", "Save");
		return "imagelog/imagelog_new";
	}

	@PostMapping("/image/log/add")
	public String save(@ModelAttribute("imagelog") MultimediaLog imagelog, @Valid MultimediaLog entity, Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("listMediaSite", mediaSiteService.findAll());
			model.addAttribute("listUser", userService.findAll());
			model.addAttribute("title", "New Image Log");
			model.addAttribute("button", "Save");
			return "imagelog/imagelog_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			imageLogService.save(imagelog);
			return "redirect:/image/log";

		}

	}

	@GetMapping("/image/log/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listMediaSite", mediaSiteService.findAll());
		model.addAttribute("listUser", userService.findAll());
		MultimediaLog imagelog = imageLogService.find(id);
		model.addAttribute("imagelog", imagelog);
		model.addAttribute("title", "Update Image Log");
		model.addAttribute("button", "Update");
		return "imagelog/imagelog_new";
	}

	@GetMapping("/image/log/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.imageLogService.delete(id);
		return "redirect:/image/log";
	}
}
