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

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.service.ConstraintRangeService;
import com.eikona.tech.service.ConstraintSingleService;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.service.OrganizationService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class MediaSiteWebController {

	@Autowired
	private MediaSiteService mediaSiteService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ConstraintRangeService constraintRangeService;

	@Autowired
	private ConstraintSingleService constraintSingleService;

	@GetMapping("/media/site")
	public String list(Model model) {
		model.addAttribute("MediaSite", mediaSiteService.findAll());
		return "mediasite/mediasite_list";
	}

	@GetMapping("/media/site/new")
	public String newMediaSite(Model model) {
		model.addAttribute("listOrganization", organizationService.findAll());
		model.addAttribute("listCaptureFrequency", constraintSingleService.findAll());
		model.addAttribute("listMediaType", constraintSingleService.findAll());
		model.addAttribute("listMediaQuality", constraintSingleService.findAll());
		model.addAttribute("listLighting", constraintSingleService.findAll());
		model.addAttribute("listAgeDemograph", constraintRangeService.findAll());
		model.addAttribute("listViewingDistance", constraintRangeService.findAll());
		MediaSite mediaSite = new MediaSite();
		model.addAttribute("mediaSite", mediaSite);
		model.addAttribute("title", "New Media Site");
		model.addAttribute("button", "Save");
		return "mediasite/mediasite_new";
	}

	@GetMapping("/media/site/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listOrganization", organizationService.findAll());
		model.addAttribute("listCaptureFrequency", constraintSingleService.findAll());
		model.addAttribute("listMediaType", constraintSingleService.findAll());
		model.addAttribute("listMediaQuality", constraintSingleService.findAll());
		model.addAttribute("listLighting", constraintSingleService.findAll());
		model.addAttribute("listAgeDemograph", constraintRangeService.findAll());
		model.addAttribute("listViewingDistance", constraintRangeService.findAll());
		MediaSite mediaSite = mediaSiteService.find(id);
		model.addAttribute("mediaSite", mediaSite);
		model.addAttribute("title", "Update Media Site");
		model.addAttribute("button", "Update");
		return "mediasite/mediasite_new";
	}

	@PostMapping("/media/site/add")
	public String save(@ModelAttribute("mediaSite") MediaSite mediaSite, @Valid MediaSite media, Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("listOrganization", organizationService.findAll());
			model.addAttribute("listCaptureFrequency", constraintSingleService.findAll());
			model.addAttribute("listMediaType", constraintSingleService.findAll());
			model.addAttribute("listMediaQuality", constraintSingleService.findAll());
			model.addAttribute("listLighting", constraintSingleService.findAll());
			model.addAttribute("listAgeDemograph", constraintRangeService.findAll());
			model.addAttribute("listViewingDistance", constraintRangeService.findAll());
			model.addAttribute("mediaSite", mediaSite);
			model.addAttribute("title", "New Media Site");
			return "mediasite/mediasite_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			mediaSiteService.save(mediaSite);
			return "redirect:/media/site";
		}
	}

	@GetMapping("/media/site/delete")
	public String delete(@PathVariable(value = "id") long id) {
		this.mediaSiteService.delete(id);
		return "redirect:/media/site";
	}
}
