package com.furca.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Runner{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long Id;
	
	@Column(name="first_name", nullable=false)
	@NotNull
    private String firstName;
    
    @Column(name="last_name", nullable=false)
    @NotNull
    private String lastName;
    
    @Column(name="birthday", nullable=false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date birthday;
    
    @OneToMany(mappedBy="runner")
    private Set<Result> results;
    
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

	@Override
	public String toString() {
		return "Runner [Id=" + Id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthday
				+ ", results=" + results + "]";
	}
	
	

}