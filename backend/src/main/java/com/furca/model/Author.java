package com.furca.model;

import java.util.Set;

import javax.persistence.*;

@Entity
public class Author {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long Id;

	String name;
	
	int age;

	@ManyToMany
	@JoinTable(name = "author_book",
		joinColumns = { @JoinColumn(name = "author_id") }, 
		inverseJoinColumns = {@JoinColumn(name = "book_id") })
	Set<Book> books;

	public Long getId() {
		return Id;
	}
	
	public void setId(Long id) {
		this.Id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "Author [Id=" + Id + ", name=" + name + ", age=" + age + ", books=" + books + "]";
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
