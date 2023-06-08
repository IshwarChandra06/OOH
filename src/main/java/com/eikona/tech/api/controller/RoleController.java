package com.eikona.tech.api.controller;

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
import com.eikona.tech.domain.Role;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.RoleDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.PrivilegeService;
import com.eikona.tech.service.RoleService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/roles")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
    @Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<Role> roleList = roleService.findAll();
		return new ResponseEntity<>(roleList, HttpStatus.OK);
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: role_add")
	@PreAuthorize("hasAuthority('role_add')")
	public ResponseEntity<Object> create(@RequestBody Role role) {
		try {
		if (null == role.getName() || role.getName().isEmpty()) {
			String message = "Name should not be empty.";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		List<Privilege> privilegeList=role.getPrivileges();
		List<Privilege> privilegeListSet=new ArrayList<Privilege>();
		for(Privilege privilege:privilegeList) {
			Privilege privilegeObj=privilegeService.find(privilege.getId());
			privilegeListSet.add(privilegeObj);
		}
		role.setPrivileges(privilegeListSet);
		Role createdRole = roleService.save(role);
		return new ResponseEntity<>(createdRole, HttpStatus.OK);
		}
		catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/id/{id}",method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: role_view")
	@PreAuthorize("hasAuthority('role_view')")
	public Role get(@PathVariable long id){
		return roleService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: role_update")
	@PreAuthorize("hasAuthority('role_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody Role role) {
		try {
			if (null == role.getId()) {
				return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
			}
			Role roleAudit = roleService.find(id);
			role.setCreatedBy(roleAudit.getCreatedBy());
			role.setCreatedDate(roleAudit.getCreatedDate());
			List<Privilege> privilegeList=role.getPrivileges();
			List<Privilege> privilegeListSet=new ArrayList<Privilege>();
			for(Privilege privilege:privilegeList) {
				Privilege privilegeObj=privilegeService.find(privilege.getId());
				privilegeListSet.add(privilegeObj);
			}
		role.setPrivileges(privilegeListSet);
		Role updatedRole = roleService.save(role);
		return new ResponseEntity<>(updatedRole, HttpStatus.OK);
		}
		 catch (Exception e) {
				String message = "Contact Admin!!";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: role_delete")
	@PreAuthorize("hasAuthority('role_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
		roleService.delete(id);
		return ResponseEntity.noContent().build();
		}
		 catch (Exception e) {
				String message = "Contact Admin!!";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
	}
	
	
	 @RequestMapping(value = "/stats", method = RequestMethod.POST)
	 @Operation(summary = "Privileges Required: role_view")
	 @PreAuthorize("hasAuthority('role_view')")
		public PaginatedDto<RoleDto> findPaginated(@RequestBody SearchRequestDto paginatedDto) {
			PaginatedDto<RoleDto> rolelistDto=null;
			List<RoleDto> roleListDto = new ArrayList<>();
			try {
				if( 0==paginatedDto.getPageNo() || 0== paginatedDto.getPageSize() ) {
					return new PaginatedDto<RoleDto>(roleListDto,
							0, 0, 0, 0, 0,"pageNo or pageSize should be greater than 0","I");
				}
			Page<Role> page = roleService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
					paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto);
					
					//findPaginated(paginatedDto.getPageNo(), paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder());

			rolelistDto = new PaginatedDto<RoleDto>(roleListDto,
					page.getTotalPages(), page.getNumber()+1, page.getSize(),page.getTotalElements(), page.getTotalElements(),"Success","S");
			}catch (Exception e) {
				return new PaginatedDto<RoleDto>(roleListDto,
						0, 0, 0, 0, 0,"Contact Admin!!","E");
			}
			return rolelistDto;
		}
		@RequestMapping(value = "/search", method = RequestMethod.POST)
		@Operation(summary = "Privileges Required: role_view")
		@PreAuthorize("hasAuthority('role_view')")
		public @ResponseBody PaginatedDto<RoleDto> search(@RequestBody SearchRequestDto paginatedDto) {

			String message="";
			String messageType="";
			PaginatedDto<RoleDto> paginatedDtoList = null;
			List<RoleDto> roleListDto = new ArrayList<>();
			try {
				
				Page<Role> page = roleService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
						paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto);
				List<Role> roleList = page.getContent();
				
				for (Role role : roleList) {
					RoleDto roleDto=new RoleDto();
					roleDto.setId(role.getId());
					roleDto.setName(role.getName());
					roleDto.setPrivileges(role.getPrivileges());
					roleListDto.add(roleDto);
				}

				List<Role> totaluserList = roleService.findAll();
				Page<Role> totalPage = new PageImpl<Role>(totaluserList);
				message = "Success";
				messageType = "S";
				paginatedDtoList = new PaginatedDto<RoleDto>(roleListDto,
						page.getTotalPages(), page.getNumber()+1, page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),message,messageType);
				
			}catch(Exception e) {
				return new PaginatedDto<RoleDto>(roleListDto,
						0, 0, 0, 0, 0,"Contact Admin!!","E");
			}
			return paginatedDtoList;
		}
		@RequestMapping(value = "/searchbyvalue", method = RequestMethod.GET)
		@Operation(summary = "Privileges Required: role_view")
		@PreAuthorize("hasAuthority('role_view')")
		public @ResponseBody List<Role> searchByValue(@RequestParam String searchValue) {
			
			List<Role> roleList = roleService.search(searchValue);
			return roleList;
			
		}
		 @RequestMapping(value = "/getorgroles", method = RequestMethod.GET)
	      public @ResponseBody List<Role> getOrgRoles() {
				List<Role> roleList = roleService.getOrgRoles();
				return roleList;
				
			}
		 @RequestMapping(value = "/getprivilegebyrole/{roleid}", method = RequestMethod.GET)
	     public @ResponseBody List<Privilege> getPrivilegeByRole(@PathVariable(value = "roleid") long id) {
				Role role =  roleService.find(id);
				List<Privilege> privilegeList=role.getPrivileges();
				return privilegeList;
				
			}
	
}