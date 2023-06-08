package com.eikona.tech.dto;

public class ConstraintRangeDto {

    private String min_value;
    
    private String max_value;
    
    private String type;
    
    private String value;

	public String getMin_value() {
		return min_value;
	}

	public String getMax_value() {
		return max_value;
	}

	public String getType() {
		return type;
	}

	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}

	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
}
