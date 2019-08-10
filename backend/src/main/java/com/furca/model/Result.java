package com.furca.model;

import java.sql.Time;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Result{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
    
    @ManyToOne
    @JoinColumn(name="run_id", nullable=false)
    @JsonBackReference
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

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public Time getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Time finishTime) {
		this.finishTime = finishTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getRunningNumber() {
		return runningNumber;
	}

	public void setRunningNumber(String runningNumber) {
		this.runningNumber = runningNumber;
	}

	@Override
	public String toString() {
		return "Result [Id=" + Id + ", run=" + run + ", runner=" + runner + ", place=" + place + ", finishTime="
				+ finishTime + ", category=" + category + ", team=" + team + ", runningNumber=" + runningNumber + "]";
	}
	
	
    
}