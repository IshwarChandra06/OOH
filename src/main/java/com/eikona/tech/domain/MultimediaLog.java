package com.eikona.tech.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name = "ooh_image_log")
@Table(name = "ooh_multimedia_log")
public class MultimediaLog extends Auditable<String>  implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    //@JsonIgnore
    @ManyToOne//(fetch = FetchType.EAGER)
    @JoinColumn(name = "media_site_id")
    private MediaSite mediaSite;
	
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    
	@Column
	private String image;
    
    @Column
    private String imageUrl;
    
    @Column
    private String thumbnailPath;
    
    @Column
    private String resizePath;
    
    @Column
    private String originalPath;
    
    @Column
    private String videoFirstFramePath;
    
    @Column
    private String videoUrl;
    
    @Column
    private Date timeStamp;
    
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getResizePath() {
		return resizePath;
	}

	public void setResizePath(String resizePath) {
		this.resizePath = resizePath;
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public String getVideoFirstFramePath() {
		return videoFirstFramePath;
	}

	public void setVideoFirstFramePath(String videoFirstFramePath) {
		this.videoFirstFramePath = videoFirstFramePath;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	

}
