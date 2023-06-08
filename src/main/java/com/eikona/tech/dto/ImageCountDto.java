package com.eikona.tech.dto;

public class ImageCountDto {
	
	private String date;

	private String assetCode;
	
	private String locality;
	
	private long totalImage;
	
	private long zeroKbImage;

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public long getTotalImage() {
		return totalImage;
	}

	public void setTotalImage(long totalImage) {
		this.totalImage = totalImage;
	}

	public long getZeroKbImage() {
		return zeroKbImage;
	}

	public void setZeroKbImage(long zeroKbImage) {
		this.zeroKbImage = zeroKbImage;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

}
