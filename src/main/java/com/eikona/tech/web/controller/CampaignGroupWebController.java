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

import com.eikona.tech.domain.CampaignGroup;
import com.eikona.tech.service.CampaignGroupService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class CampaignGroupWebController {

	@Autowired
	private CampaignGroupService campaignGroupService;

	@GetMapping("/campaign/group")
	public String list(Model model) {
		model.addAttribute("CampainGroup", campaignGroupService.findAll());
		return "campaigngroup/campaigngroup_list";
	}

	@GetMapping("/campaign/group/new")
	public String newCampaignGroup(Model model) {
		CampaignGroup campaignGroup = new CampaignGroup();
		model.addAttribute("campaigngroup", campaignGroup);
		model.addAttribute("title", "New Campaign Group");
		model.addAttribute("button", "Save");
		return "campaigngroup/campaigngroup_new";
	}

	@PostMapping("/campaign/group/add")
	public String save(@ModelAttribute("campaigngroup") CampaignGroup campaigngroup, @Valid CampaignGroup entity,
			Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute("title", "New Campaign Group");
			model.addAttribute("button", "Save");
			return "campaigngroup/campaigngroup_new";
		} else {
			model.addAttribute("message", "Add Successfully");
			campaignGroupService.save(campaigngroup);
			return "redirect:/campaign/group";

		}

	}

	@GetMapping("/campaign/group/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		CampaignGroup campaignGroup = campaignGroupService.find(id);
		model.addAttribute("campaigngroup", campaignGroup);
		model.addAttribute("title", "Update Campaign Group");
		model.addAttribute("button", "Update");
		return "campaigngroup/campaigngroup_new";
	}

	@GetMapping("/campaign/group/delete/{id}")
	public String delete(@PathVariable(value = "id") long id) {
		this.campaignGroupService.delete(id);
		return "redirect:/campaign/group";
	}
}
