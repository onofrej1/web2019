package com.furca.model;

import java.util.Set;

import javax.persistence.*;

@Entity
public class Book{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
	
    private String title;
    
    private String description;
    
	@ManyToMany(mappedBy = "books")
    Set<Author> authors;

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
	
	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}


}