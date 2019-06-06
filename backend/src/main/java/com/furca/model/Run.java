package com.furca.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Run{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
	
	@Column(name="name")
    private String name;
    
    @Column(name="run_date")
    @Basic
    @Temporal(TemporalType.DATE)
    private java.util.Date runDate;
    
    @Column(name="edition")
    private int edition;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="event_id", nullable=false)
    private Event event;
    
    @Override
	public String toString() {
		return "Run [Id=" + Id + ", name=" + name + ", runDate=" + runDate + ", edition=" + edition + ", event=" + event
				+ ", results=" + results + "]";
	}

	@OneToMany(mappedBy="run")
    private Set<Result> results;

	public int getId() {
		return Id;
	}
	
	public void setId(int Id) {
		this.Id = Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Set<Result> getResults() {
		return results;
	}

	public void setResults(Set<Result> results) {
		this.results = results;
	}
    
    
}