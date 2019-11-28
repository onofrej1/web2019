package com.furca.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Event{
	
	@Override
	public String toString() {
		return "Event [Id=" + id + ", name=" + name + ", locality=" + locality + ", runs=" + runs + "]";
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Column(name="name", nullable=false, unique=true)
	@NotNull
    private String name;
	
	@Column(name="locality", nullable=false)
	@NotNull
    private String locality;
    
	@OneToMany(mappedBy="event")
	@OrderBy(value = "run_date ASC")
	@JsonBackReference
    private Set<Run> runs;

	public List<Long> getRunIds() {
		return this.getRuns()
			.stream()
			.map(Run::getId)
			.collect(Collectors.toList());
	}
}