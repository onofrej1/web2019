package com.furca.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

@Entity
public class Event{
	
	@Override
	public String toString() {
		return "Event [Id=" + Id + ", name=" + name + ", locality=" + locality + ", runs=" + runs + "]";
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
	
	@Column(name="name")
    private String name;
	
	@Column(name="locality")
    private String locality;
    
	@OneToMany(mappedBy="event", cascade=CascadeType.ALL)
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
	
	public List<Integer> getRunIds() {
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