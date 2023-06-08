package com.eikona.tech.api.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.Privilege;
import com.eikona.tech.domain.Role;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.OrganizationDto;
import com.eikona.tech.dto.OrganizationImportDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.RoleDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.OrganizationRepository;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.OrganizationService;
import com.eikona.tech.service.RoleService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	Logger logger=LoggerFactory.getLogger(OrganizationController.class);
	
	@Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<Organization> orgList = organizationService.findAll();
		return new ResponseEntity<>(orgList, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: organization_add")
	@PreAuthorize("hasAuthority('organization_add')")
	public ResponseEntity<Object> create(@RequestBody Organization organization, Principal principal) {
		try {
			if (null == organization.getName() || organization.getName().isEmpty()) {
				String message = "Name should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (null == organization.getRoles() || organization.getRoles().isEmpty()) {
				String message = "Role should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}

			Organization org = organizationRepository.findByName(organization.getName());
			if (null != org) {
				return new ResponseEntity<>("Input organization is already exist.", HttpStatus.BAD_REQUEST);
			}

			List<Role> roleListSet = new ArrayList<Role>();
			for (Role role : organization.getRoles()) {
				Role roleobj = roleService.find(role.getId());
				roleListSet.add(roleobj);
			}
			organization.setRoles(roleListSet);

			String username = principal.getName();
			User user = userRepository.findByUserName(username).get();

			organization.setCreateByOrganization(user.getOrganization().getId());
			Organization createdOrganization = organizationService.save(organization);
			return new ResponseEntity<>(createdOrganization, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/createten", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: organization_add")
	@PreAuthorize("hasAuthority('organization_add')")
	public ResponseEntity<Object> createTen(@RequestBody List<Organization> organization, Principal principal) {
		try {
			if (null == organization || organization.isEmpty()) {
				String message = "Name and Role should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			String username = principal.getName();
			User user = userRepository.findByUserName(username).get();
			List<Organization> organizationList = new ArrayList<>();
			for (Organization org : organization) {

				if (null == org.getName() || org.getName().isEmpty()) {
					String message = "Name should not be empty.";
					return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
				}
				if (null == org.getRoles() || org.getRoles().isEmpty()) {
					String message = "Role should not be empty.";
					return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
				}

				List<Role> roleList = org.getRoles();
				List<Role> roleListSet = new ArrayList<Role>();
				for (Role role : roleList) {
					Role roleobj = roleService.find(role.getId());
					roleListSet.add(roleobj);
				}
				org.setRoles(roleListSet);
				org.setCreateByOrganization(user.getOrganization().getId());
				organizationList.add(org);
			}

			List<Organization> createdOrganization = (List<Organization>) organizationRepository
					.saveAll(organizationList);
			return new ResponseEntity<>(createdOrganization, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: organization_view")
	@PreAuthorize("hasAuthority('organization_view')")
	public Organization get(@PathVariable long id) {
		return organizationService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: organization_update")
	@PreAuthorize("hasAuthority('organization_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody Organization organization,
			Principal principal) {
		try {
			if (null == organization.getId()) {
				return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
			}
			if (null == organization.getName() || organization.getName().isEmpty()) {
				String message = "Name should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (null == organization.getRoles() || organization.getRoles().isEmpty()) {
				String message = "Role should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			List<Role> roleListSet = new ArrayList<Role>();
			for (Role role : organization.getRoles()) {
				Role roleobj = roleService.find(role.getId());
				roleListSet.add(roleobj);
			}
			organization.setRoles(roleListSet);
			String username = principal.getName();
			User user = userRepository.findByUserName(username).get();
			organization.setCreateByOrganization(user.getOrganization().getId());

			Organization auditOrganization = organizationService.find(id);
			organization.setCreatedBy(auditOrganization.getCreatedBy());
			organization.setCreatedDate(auditOrganization.getCreatedDate());

			Organization updatedOrganization = organizationService.save(organization);
			return new ResponseEntity<>(updatedOrganization, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: organization_delete")
	@PreAuthorize("hasAuthority('organization_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
			organizationService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

	}

	// @Hidden
	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: organization_view")
	@PreAuthorize("hasAuthority('organization_view')")
	public PaginatedDto<OrganizationDto> findPaginated(@RequestBody SearchRequestDto paginatedDto,
			Principal principal) {
		PaginatedDto<OrganizationDto> paginatedDtoList = null;
		List<OrganizationDto> organizationList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<OrganizationDto>(organizationList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}
			Page<Organization> page = organizationService.searchByField(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto,
					principal);

			paginatedDtoList = new PaginatedDto<OrganizationDto>(organizationList, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalElements(), "success", "S");

		} catch (Exception e) {
			return new PaginatedDto<OrganizationDto>(organizationList, 0, 0, 0, 0, 0, "Failed", "E");
		}

		return paginatedDtoList;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: organization_view")
	@PreAuthorize("hasAuthority('organization_view')")
	public @ResponseBody PaginatedDto<OrganizationDto> search(@RequestBody SearchRequestDto paginatedDto,
			Principal principal) {

		String message = "";
		String messageType = "";
		PaginatedDto<OrganizationDto> paginatedDtoList = null;
		List<OrganizationDto> organizationList = new ArrayList<>();
		try {
			if (null == paginatedDto.getOrgId()) {
				return new PaginatedDto<OrganizationDto>(organizationList, 0, 0, 0, 0, 0,
						"OrgId is compulsory for searching", "I");
			}
			Page<Organization> page = organizationService.searchByField(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto,
					principal);
			List<Organization> orgList = page.getContent();

			for (Organization organization : orgList) {
				OrganizationDto organizationDto = new OrganizationDto();
				organizationDto.setId(organization.getId());
				organizationDto.setName(organization.getName());
				organizationDto.setAddress(organization.getAddress());
				organizationDto.setCity(organization.getCity());
				organizationDto.setCountry(organization.getCountry());
				organizationDto.setPinCode(organization.getPinCode());
				organizationDto.setPocEmail(organization.getPocEmail());
				organizationDto.setPointOfContact(organization.getPointOfContact());
				organizationDto.setState(organization.getState());
				organizationDto.setPocPhone(organization.getPocPhone());

//				List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
//				for (Role role : organization.getRoles()) {
//					RoleDto roleDto = new RoleDto();
//					roleDto.setId(role.getId());
//					roleDto.setName(role.getName());
//
//					roleDtoList.add(roleDto);
//				}
				
				List<RoleDto> roleDtoList = organization.getRoles().stream().map(role ->{
					RoleDto roleDto = new RoleDto();
					roleDto.setId(role.getId());
					roleDto.setName(role.getName());
					return roleDto;
				}).collect(Collectors.toList());
				
					
				organizationDto.setRoles(roleDtoList);

				organizationList.add(organizationDto);

			}

			List<Organization> totalOrgList = organizationService.findAll();
			Page<Organization> totalPage = new PageImpl<Organization>(totalOrgList);
			message = "Success";
			messageType = "S";
			paginatedDtoList = new PaginatedDto<OrganizationDto>(organizationList, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), page.getTotalElements(), totalPage.getTotalElements(),
					message, messageType);

		} catch (Exception e) {
			return new PaginatedDto<OrganizationDto>(organizationList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}

	@RequestMapping(value = "/searchbyvalue", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: organization_view")
	@PreAuthorize("hasAuthority('organization_view')")
	public @ResponseBody List<Organization> searchbyvalue(@RequestParam String searchValue) {

		List<Organization> organizationList = organizationService.search(searchValue);
		return organizationList;

	}

	@RequestMapping(value = "/getorganizationbyrole/{roleId}", method = RequestMethod.GET)
	public @ResponseBody List<Organization> getOrganizationByRole(@PathVariable(value = "roleId") Long roleId,
			Principal principal) {

		List<Organization> organizationList = organizationService.getOrganizationByRole(roleId, principal);

		return organizationList;
	}

	@RequestMapping(value = "/getprivilegesbyorganization/{orgId}", method = RequestMethod.GET)
	public @ResponseBody List<Privilege> getPrivilegesByOrg(@PathVariable(value = "orgId") Long orgId) {

		List<Privilege> privilegeList = organizationService.getPrivilegesByOrganization(orgId);

		return privilegeList;
	}

	@RequestMapping(value = "/importorganization", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: organization_add")
	@PreAuthorize("hasAuthority('organization_add')")
	public ResponseEntity<Object> importOrg(@RequestBody List<OrganizationImportDto> organization,
			Principal principal) {

		try {
			String message = organizationService.importOrg(organization, principal);

			if ("Import Success!!".equalsIgnoreCase(message)) {
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}
}
