package com.furca.model;

import java.sql.Time;
import java.util.Set;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Result{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
    
    @ManyToOne
    @JoinColumn(name="run_id", nullable=false)
    @JsonManagedReference
    private Run run;
    
    @ManyToOne
    @JoinColumn(name="runner_id", nullable=false)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Runner runner;
    
    @Column(name="place")
    private int place;
    
    @Column(name="finish_time")
    private Time finishTime;
    
    @Column(name="category")
    private String category;
    
    @Column(name="team")
    private String team;
    
    @Column(name="running_number")
    private String runningNumber;
	
	@Override
	public String toString() {
		return "Result [Id=" + Id + ", run=" + run + ", runner=" + runner + ", place=" + place + ", finishTime="
				+ finishTime + ", category=" + category + ", team=" + team + ", runningNumber=" + runningNumber + "]";
	}
	
	
    
}