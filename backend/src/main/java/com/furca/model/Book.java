package com.furca.model;

import javax.persistence.*;

@Entity
public class Book{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
	
    private String title;
    
    private String description;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}