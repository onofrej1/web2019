package com.furca.model;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Run implements Specifiable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Column(name="name")
    private String name;
    
    @Column(name="run_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date runDate;
    
    @Column(name="edition", nullable=false)
    private int edition;
    
    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;
    
    @Override
	public String toString() {
		return "Run [Id=" + id + ", name=" + name + ", runDate=" + runDate + ", edition=" + edition + ", event=" + event
				+ ", results=" + results + "]";
	}       

	@OneToMany(mappedBy="run")
    private Set<Result> results;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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