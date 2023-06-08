package com.eikona.tech.dto;

public class SearchRequestDto {
	
	private Object searchData;
	private String sortField;
	private String sortOrder;
	private int pageNo;
	private int pageSize;
	private Long orgId;
	private String sDate;
	private String eDate;
	
	public SearchRequestDto() {
		super();
	}

	
	public SearchRequestDto(Object searchData, String sortField, String sortOrder, int pageNo, int pageSize) {
		super();
		this.searchData = searchData;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}


	public Object getSearchData() {
		return searchData;
	}

	public void setSearchData(Object searchData) {
		this.searchData = searchData;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public Long getOrgId() {
		return orgId;
	}


	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}


	public String getsDate() {
		return sDate;
	}


	public void setsDate(String sDate) {
		this.sDate = sDate;
	}


	public String geteDate() {
		return eDate;
	}


	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	
	
}
