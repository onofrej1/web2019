package com.furca.model;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "myRun", types = { Run.class }) 
public interface MyRun {
  Long getId();
  String getName();
  Date getRunDate();
  int getEdition();
  Event getEvent();
}
