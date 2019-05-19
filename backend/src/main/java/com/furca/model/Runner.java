package com.furca.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Runner{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
	
	@Column(name="first_name")
    private String firstName;
    
    @Column(name="last_name")
    private String lastName;
    
    @Temporal(TemporalType.DATE)
    @Column(name="birthday")
    private Date birthday;
    
    @OneToMany(mappedBy="runner")
    private Set<Result> results;
    
    public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Set<Result> getResults() {
		return results;
	}

	public void setResults(Set<Result> results) {
		this.results = results;
	}

	
	
}