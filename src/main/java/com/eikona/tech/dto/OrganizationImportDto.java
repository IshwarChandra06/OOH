package com.eikona.tech.dto;

public class OrganizationImportDto {

	private String name;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pinCode;
	
	private String pointOfContact;
	
	private String pocEmail;
	
	private String pocPhone;
	
	private String createByOrganization;
	
	private String roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	public String getPocEmail() {
		return pocEmail;
	}

	public void setPocEmail(String pocEmail) {
		this.pocEmail = pocEmail;
	}

	public String getPocPhone() {
		return pocPhone;
	}

	public void setPocPhone(String pocPhone) {
		this.pocPhone = pocPhone;
	}

	public String getCreateByOrganization() {
		return createByOrganization;
	}

	public void setCreateByOrganization(String createByOrganization) {
		this.createByOrganization = createByOrganization;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
}
