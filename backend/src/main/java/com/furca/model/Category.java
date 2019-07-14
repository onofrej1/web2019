package com.furca.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Category {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long Id;
	
	@Column(name="category", nullable=false)
	@NotNull
    private String category;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCatgory(String category) {
		this.category = category;
	}
	
	
}
