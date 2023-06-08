package com.eikona.tech.api.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

import com.eikona.tech.domain.Privilege;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.PrivilegeDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.PrivilegeService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/privilege")
public class PrivilegeController {
	@Autowired
	private PrivilegeService privilegeService;
    @Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<Privilege> privilegeList = privilegeService.findAll();
		return new ResponseEntity<>(privilegeList, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: privilege_add")
	@PreAuthorize("hasAuthority('privilege_add')")
	public ResponseEntity<Object> create(@RequestBody Privilege privilege) {
		try {
			if (null == privilege.getName() || privilege.getName().isEmpty()) {
				String message = "Name should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			Privilege createdPrivilege = privilegeService.save(privilege);
			return new ResponseEntity<>(createdPrivilege, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: privilege_view")
	@PreAuthorize("hasAuthority('privilege_view')")
	public Privilege get(@PathVariable long id) {
		return privilegeService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: privilege_update")
	@PreAuthorize("hasAuthority('privilege_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody Privilege privilege) {
		try {
			if (null == privilege.getId()) {
				return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
			}
			Privilege privilegeAudit = privilegeService.find(id);
			privilege.setCreatedBy(privilegeAudit.getCreatedBy());
			privilege.setCreatedDate(privilegeAudit.getCreatedDate());
			Privilege updatedPrivilege = privilegeService.save(privilege);
			return new ResponseEntity<>(updatedPrivilege, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: privilege_delete")
	@PreAuthorize("hasAuthority('privilege_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
			privilegeService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: privilege_view")
	@PreAuthorize("hasAuthority('privilege_view')")
	public PaginatedDto<PrivilegeDto> findPaginated(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<PrivilegeDto> rolelistDto = null;
		List<PrivilegeDto> roleListDto = new ArrayList<>();
		try {
			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<PrivilegeDto>(roleListDto, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}
			Page<Privilege> page = privilegeService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(),
					paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto);

			rolelistDto = new PaginatedDto<PrivilegeDto>(roleListDto, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(), 0, page.getTotalElements(), "Success", "S");
		} catch (Exception e) {
			return new PaginatedDto<PrivilegeDto>(roleListDto, 0, 0, 0, 0, 0, "Contact Admin!!", "E");
		}
		return rolelistDto;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: privilege_view")
	@PreAuthorize("hasAuthority('privilege_view')")
	public @ResponseBody PaginatedDto<PrivilegeDto> search(@RequestBody SearchRequestDto paginatedDto) {

		String message = "";
		String messageType = "";
		PaginatedDto<PrivilegeDto> paginatedDtoList = null;
		List<PrivilegeDto> privilegeListDto = new ArrayList<>();
		try {
			
			Page<Privilege> page = privilegeService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(),
					paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto);
			List<Privilege> privilegeList = page.getContent();

			for (Privilege privilege : privilegeList) {
				PrivilegeDto privilegeDto = new PrivilegeDto();
				privilegeDto.setId(privilege.getId());
				privilegeDto.setName(privilege.getName());

				privilegeListDto.add(privilegeDto);
			}

			List<Privilege> totaluserList = privilegeService.findAll();
			Page<Privilege> totalPage = new PageImpl<Privilege>(totaluserList);
			message = "Success";
			messageType = "S";
			paginatedDtoList = new PaginatedDto<PrivilegeDto>(privilegeListDto, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), page.getTotalElements(), totalPage.getTotalElements(),
					message, messageType);

		} catch (Exception e) {
			return new PaginatedDto<PrivilegeDto>(privilegeListDto, 0, 0, 0, 0, 0, "Contact Admin!!", "E");
		}
		return paginatedDtoList;
	}

	@RequestMapping(value = "/searchbyvalue", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: privilege_view")
	@PreAuthorize("hasAuthority('privilege_view')")
	public @ResponseBody List<Privilege> searchByValue(@RequestParam String searchValue) {

		List<Privilege> privilegeList = privilegeService.search(searchValue);
		return privilegeList;

	}
}
