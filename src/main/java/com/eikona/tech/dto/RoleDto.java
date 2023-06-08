package com.eikona.tech.dto;

import java.util.List;

import com.eikona.tech.domain.Privilege;

public class RoleDto {
	     private Long id;
	     
	    private String name;
	    
	    private List<Privilege> privileges;
	    
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public List<Privilege> getPrivileges() {
			return privileges;
		}

		public void setPrivileges(List<Privilege> privileges) {
			this.privileges = privileges;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	    
}
