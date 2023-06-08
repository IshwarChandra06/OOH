package com.eikona.tech.dto;

import java.util.Date;

public class MediaSiteImageLogDto {
	
	private String mediaSiteName;
	private Date date;
	private String image;
	
	
	public String getMediaSiteName() {
		return mediaSiteName;
	}
	public void setMediaSiteName(String mediaSiteName) {
		this.mediaSiteName = mediaSiteName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public MediaSiteImageLogDto() {
		super();
	}
	public MediaSiteImageLogDto(String mediaSiteName, Date date, String image) {
		super();
		this.mediaSiteName = mediaSiteName;
		this.date = date;
		this.image = image;
	}
}
