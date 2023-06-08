package com.eikona.tech.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="ooh_role")
public class Role extends Auditable<String> implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    @Column(name = "name")
    private String name;
    
    @JsonIgnore
    @Column(columnDefinition = "BIT DEFAULT 0",length=1)
    private boolean isOrgRole;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Privilege> privileges;

    @Column
    private boolean isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isOrgRole() {
		return isOrgRole;
	}

	public void setOrgRole(boolean isOrgRole) {
		this.isOrgRole = isOrgRole;
	}

	
	public Role(String name, List<Privilege> privileges, boolean isDeleted) {
		this.name = name;
		this.privileges = privileges;
		this.isDeleted = isDeleted;
	}

	public Role() {
		super();
	}
	
	
}
