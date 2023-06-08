package com.eikona.tech.dto;

import java.util.List;

public class AssetReportDto<T> {

	private List<T> data;
	private int occupiedDays;
	private int emptyDays;
	private int totalDays;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getOccupiedDays() {
		return occupiedDays;
	}

	public void setOccupiedDays(int occupiedDays) {
		this.occupiedDays = occupiedDays;
	}

	public int getEmptyDays() {
		return emptyDays;
	}

	public void setEmptyDays(int emptyDays) {
		this.emptyDays = emptyDays;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

}
