package com.eikona.tech.dto;

import com.eikona.tech.domain.User;

public class FeedbackDto {

	    private User user;

	    private String subject;
	    
	    private String body;

		public User getUser() {
			return user;
		}

		public String getSubject() {
			return subject;
		}

		public String getBody() {
			return body;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public void setBody(String body) {
			this.body = body;
		}
	    
}
