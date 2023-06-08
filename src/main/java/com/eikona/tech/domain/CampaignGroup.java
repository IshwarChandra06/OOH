package com.eikona.tech.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "ooh_campaign_group")
public class CampaignGroup extends Auditable<String> implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    @NotBlank(message="Campaign Group Name is mandatory")
    @Column(name = "name",nullable = false)
    private String name;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="campaign_grouping", joinColumns={@JoinColumn(name="campaign_group_id", referencedColumnName="ID")}, 
    inverseJoinColumns={@JoinColumn(name="campaign_id", referencedColumnName="ID")})
    private List<Campaign> campaign;
    
    @Column
    private boolean isDeleted;

    public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Campaign> getCampaign() {
		return campaign;
	}

	public void setCampaign(List<Campaign> campaign) {
		this.campaign = campaign;
	}

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
	

}