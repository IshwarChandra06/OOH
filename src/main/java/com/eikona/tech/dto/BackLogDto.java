package com.eikona.tech.dto;

import java.util.Date;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.User;

public class BackLogDto {
	private MediaSite mediaSite;
	private User user;
	private Date date;
	private String Reason;
	
	
	public MediaSite getMediaSite() {
		return mediaSite;
	}
	public User getUser() {
		return user;
	}
	public Date getDate() {
		return date;
	}
	public String getReason() {
		return Reason;
	}
	public void setMediaSite(MediaSite mediaSite) {
		this.mediaSite = mediaSite;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	
	
	 
	 
}
