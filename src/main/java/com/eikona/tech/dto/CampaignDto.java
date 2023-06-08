package com.eikona.tech.dto;

import java.util.Date;
import java.util.List;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.Organization;

public class CampaignDto {
	private Long id;

	private String name;

	List<MediaSite> mediasite;

	private Organization agency;

	private Organization brand;

	private Organization organization;

	private Date startDate;

	private Date endDate;
	
	private boolean ownedAsset;

	public String getName() {
		return name;
	}

	public Organization getOrganization() {
		return organization;
	}

	public Date getStartDate() {
		return startDate;
	}

	public boolean isOwnedAsset() {
		return ownedAsset;
	}

	public void setOwnedAsset(boolean ownedAsset) {
		this.ownedAsset = ownedAsset;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MediaSite> getMediasite() {
		return mediasite;
	}

	public void setMediasite(List<MediaSite> mediasite) {
		this.mediasite = mediasite;
	}

	public Organization getAgency() {
		return agency;
	}

	public void setAgency(Organization agency) {
		this.agency = agency;
	}

	public Organization getBrand() {
		return brand;
	}

	public void setBrand(Organization brand) {
		this.brand = brand;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
