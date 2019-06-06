package com.furca.model;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "myEvent", types = { Event.class }) 
public interface MyEvent {
  Long getId();
  String getName();
  String getLocality();
  Set<Run> getRuns();
}

