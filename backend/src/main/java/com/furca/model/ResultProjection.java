package com.furca.model;

import java.sql.Time;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "ResultProjection", types = { Result.class }) 
public interface ResultProjection {
  Long getId();  
  int getPlace();  
  String getCategory();
  String getTeam();
  String getRunningNumber();
  Time getFinishTime();
  Runner getRunner();
  Run getRun();
}
