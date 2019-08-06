package com.furca.dto;

import java.sql.Time;

public class ResultDto {
	
	private Long runnerId;
    
    private int place;
	
    private String runningNumber;
    
    private String category;
    
    private String team;
    
    private Time finishTime;

	public Long getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public String getRunningNumber() {
		return runningNumber;
	}

	public void setRunningNumber(String runningNumber) {
		this.runningNumber = runningNumber;
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

	public Time getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Time finishTime) {
		this.finishTime = finishTime;
	}

	
}
