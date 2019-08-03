package com.furca.web;

import java.util.List;

import com.furca.dto.RunnerDto;

public class RunnerListContainer {
	
	private List<RunnerDto> runners;

    public List<RunnerDto> getRunners() {
        return runners;
    }

    public void setRunners(List<RunnerDto> runners) {
        this.runners = runners;
    }

	@Override
	public String toString() {
		return "RunnerListContainer [runners=" + runners + "]";
	}
    
    

}
