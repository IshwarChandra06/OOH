package com.eikona.tech.dto;

import java.util.List;

import com.eikona.tech.domain.Campaign;

public class CampaignGroupDto {
	private Long id;
	
	private String name;
	
	private List<Campaign> campaign;
	
	public List<Campaign> getCampaign() {
		return campaign;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCampaign(List<Campaign> campaign) {
		this.campaign = campaign;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
