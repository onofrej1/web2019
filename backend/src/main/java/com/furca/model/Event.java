package com.furca.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Event{
	
	@Override
	public String toString() {
		return "Event [Id=" + Id + ", name=" + name + ", locality=" + locality + ", runs=" + runs + "]";
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long Id;
	
	@Column(name="name", nullable=false, unique=true)
	@NotNull
    private String name;
	
	@Column(name="locality", nullable=false)
	@NotNull
    private String locality;
    
	@OneToMany(mappedBy="event", cascade=CascadeType.ALL)
	@OrderBy(value = "run_date ASC")
    private Set<Run> runs;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
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
	
	public List<Long> getRunIds() {
		return this.getRuns()
			.stream()
			.map(Run::getId)
			.collect(Collectors.toList());
	}
	
	/*public void addRun(Run run) {
        runs.add(run);
        run.setEvent(this);
    }
 
    public void removeRun(Run run) {
    	run.setEvent(null);
        runs.remove(run);
    }*/
	
	
}