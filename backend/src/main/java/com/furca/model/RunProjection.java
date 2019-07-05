package com.furca.model;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "RunProjection", types = { Run.class }) 
public interface RunProjection {
  Long getId();
  String getName();
  Date getRunDate();
  int getEdition();
  Event getEvent();
}
