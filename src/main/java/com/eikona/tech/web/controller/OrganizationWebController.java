//package com.eikona.tech.web.controller;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
//import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.eikona.tech.domain.Organization;
//import com.eikona.tech.service.OrganizationService;
//
//@Controller
//public class OrganizationWebController {
//
//
//	@Autowired
//	private OrganizationService organizationService;
//	
//	@GetMapping("/organization")
//	public String list(Model model) {
//		model.addAttribute("Organization",organizationService.findAll());
//		return "organization/organization_list";
//	}
//	
//	@GetMapping("/organization/new")
//	public String newOrganization(Model model) {
//		model.addAttribute("listOrganization",organizationService.findAll());
//		Organization organization=new Organization();
//		model.addAttribute("organization",organization);
//		model.addAttribute("title","New Organization");
//		model.addAttribute("button","Save");
//		return "organization/organization_new";
//	}
//	
//	@GetMapping("/organization/edit/{id}")
//	public String edit(@PathVariable(value = "id") long id, Model model) {
//		model.addAttribute("listOrganization", organizationService.findAll());
//		Organization organization=organizationService.find(id);
//		model.addAttribute("organization",organization );
//		model.addAttribute("title","Update Organization");
//		model.addAttribute("button","Update");
//		return "organization/organization_new";
//	}
//	
//	@PostMapping("/organization/add")
//	public String save(@ModelAttribute("organization") Organization organization, @Valid Organization organisation, Errors errors,
//			Model model) {
//		if (errors.hasErrors()) {
//			model.addAttribute("title","New Organization");
//			model.addAttribute("button","Save");
//			return "organization/organization_new";
//		} else {
//			model.addAttribute("message", "Add Successfully");
//			if(organization.getOrganization()==null) {
//				organizationService.save(organization);
//			}
//			else {
//				Organization neworganization=new Organization();
//				neworganization.setId(organization.getId());
//				neworganization.setName(organization.getName());
//				neworganization.setOrganization(organization.getOrganization());
//				neworganization.setOrganizationType(organization.getOrganizationType());
//				organizationService.save(neworganization);
//			}
//			
//			return "redirect:/organization";
//		}
//	}
//	
//	@GetMapping("/organization/delete")
//	public String delete(@PathVariable(value = "id") long id) {
//		this.organizationService.delete(id);
//		return "redirect:/organization";
//	}
//	@GetMapping(value = "/api/organization")
//	public @ResponseBody DataTablesOutput<Organization> organization(@Valid DataTablesInput input) {
//		// return contractorRepository.findAll(input);
//
//		return (DataTablesOutput<Organization>) organizationService.getAll(input);
//	}
//}
