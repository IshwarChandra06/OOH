package com.eikona.tech.dto;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.Ticket.Status;
import com.eikona.tech.domain.User;

public class TicketDto {
	
    private MediaSite mediaSite;
	
    private User user;
    
    private String subject;
    
    private String body;
    
    private Status status;

	public MediaSite getMediaSite() {
		return mediaSite;
	}

	public void setMediaSite(MediaSite mediaSite) {
		this.mediaSite = mediaSite;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
    
}
