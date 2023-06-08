package com.eikona.tech.domain;


import java.util.Date;

import javax.persistence.Column;
//import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {

	@Schema(hidden = true)
    @CreatedBy
    @Column(name = "created_by")
    private U createdBy;

	@Schema(hidden = true)
    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

	@Schema(hidden = true)
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private U lastModifiedBy;

	@Schema(hidden = true)
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;


	public U getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(U createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public U getLastModifiedBy() {
		return lastModifiedBy;
	}


	public void setLastModifiedBy(U lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}


	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
    
    
}