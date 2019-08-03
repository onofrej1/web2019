package com.furca.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.furca.model.Runner;

public class RunnerDto {
	
    private String guid;
    
    private Long runnerId;
	
    private String firstName;
    
    private String lastName;
    
    private Date birthday;
    
    private List<Runner> names = new ArrayList<Runner>();

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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public List<Runner> getNames() {
		return names;
	}
	
	public void addName(Runner name) {
		System.out.println("ad name");
		this.names.add(name);
	}

	public void setNames(List<Runner> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "Runner [Id=" + guid + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=";
	}
	
	

}
