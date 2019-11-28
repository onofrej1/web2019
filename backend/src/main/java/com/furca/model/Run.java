package com.furca.model;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Run implements Searchable {
	
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
    @JsonManagedReference
    private Event event;
    
    @Override
	public String toString() {
		return "Run [Id=" + id + ", name=" + name + ", runDate=" + runDate + ", edition=" + edition + ", event=" + event
				+ ", results=" + results + "]";
	}       

	@OneToMany(mappedBy="run")
	@JsonBackReference
    private Set<Result> results;
}