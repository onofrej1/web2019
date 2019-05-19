package com.furca.model;

import java.util.Set;
import javax.persistence.*;

@Entity
public class Event{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
	
	@Column(name="name")
    private String name;
	
	@Column(name="locality")
    private String locality;
    
	@OneToMany(mappedBy="event")
    private Set<Run> runs;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public Set<Run> getRuns() {
		return runs;
	}

	public void setRuns(Set<Run> runs) {
		this.runs = runs;
	}
	
	
}