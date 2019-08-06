package com.furca.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.furca.model.Runner;

public class RunnerDto {
	
    private String guid;
    
    private Long runnerId;
	
    private String firstName;
    
    private String lastName;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthdate;
    
    private List<RunnerDto> names = new ArrayList<RunnerDto>();

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}


	public Long getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public List<RunnerDto> getNames() {
		return names;
	}
	
	public void addName(RunnerDto name) {
		System.out.println("add name");
		this.names.add(name);
	}

	public void setNames(List<RunnerDto> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "Runner [Id=" + guid + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=";
	}
	
	

}
