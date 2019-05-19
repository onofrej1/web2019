package com.furca.model;

import java.sql.Time;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Result{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
    
    @ManyToOne
    @JoinColumn(name="run_id", nullable=false)
    @JsonBackReference
    private Run run;
    
    @ManyToOne
    @JoinColumn(name="runner_id", nullable=false)
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

	public int getId() {
		return Id;
	}

	public void setId(int id) {
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
    
}