package com.eikona.tech.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity

public class ConstraintRange implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    @NotBlank(message="Min Value is mandatory")
    @Column(name = "min_val", nullable = false)
    private String min_value;
    
    @NotBlank(message="Max Value is mandatory")
    @Column(name = "max_val", nullable = false)
    private String max_value;
    
    
    @NotBlank
    @Column(name = "type", nullable = false)
    private String type;
    
    @NotBlank
    @Column(name = "value", nullable = false)
    private String value;
    
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


	public String getMin_value() {
		return min_value;
	}


	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}


	public String getMax_value() {
		return max_value;
	}


	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}


	public String getType() {
		return type;
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
