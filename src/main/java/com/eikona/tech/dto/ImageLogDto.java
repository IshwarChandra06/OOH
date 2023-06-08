package com.eikona.tech.dto;

import java.util.Date;

import javax.persistence.Column;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.User;

public class ImageLogDto {

	private MediaSite mediaSite;
	
    private User user;
    
   	private String image;
    
    private String imageUrl;
    
    private Date timeStamp;
    
    private String assetCode;
    
    private String thumbnailUrl;
    
    private String originalUrl;
    
    private String videoFirstFramePath;
    
    private String videoUrl;

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

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setThumbnailUrl(String thumbnailsUrl) {
		this.thumbnailUrl = thumbnailsUrl;
	}

	public void setOriginalUrl(String orginalUrl) {
		this.originalUrl = orginalUrl;
	}

	public String getVideoFirstFramePath() {
		return videoFirstFramePath;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoFirstFramePath(String videoFirstFramePath) {
		this.videoFirstFramePath = videoFirstFramePath;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}
