package com.eikona.tech.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;


@Entity(name = "ooh_campaign")
public class Campaign extends Auditable<String> implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    @NotBlank(message="Campaign Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToMany
    List<MediaSite> mediasite;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_org_id", nullable = false)
    private Organization agency;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_org_id", nullable = false)
    private Organization brand;
    
    
    @Column(name = "owned_asset")
    private boolean ownedAsset;
    
    @Column
    private Date startDate;
    
    @Column
    private Date endDate;
    
    
    @Column
    private boolean isDeleted;

    public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	public boolean isOwnedAsset() {
		return ownedAsset;
	}

	public void setOwnedAsset(boolean ownedAsset) {
		this.ownedAsset = ownedAsset;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

}