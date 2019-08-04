package com.furca.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Runner{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
	
	@Column(name="first_name", nullable=false)
	@NotNull
    private String firstName;
    
    @Column(name="last_name", nullable=false)
    @NotNull
    private String lastName;
    
    @Column(name="birthdate", nullable=false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date birthdate;
    
    @OneToMany(mappedBy="runner")
    private Set<Result> results = new HashSet<Result>();
    
    public Long getId() {
		return Id;
	}

	public void setId(Long id) {
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Set<Result> getResults() {
		return results;
	}

	public void setResults(Set<Result> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Runner [Id=" + Id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthdate
				+ ", results=" + results + "]";
	}
	
	

}