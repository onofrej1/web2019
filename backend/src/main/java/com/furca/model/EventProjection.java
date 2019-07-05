package com.furca.model;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "EventProjection", types = { Event.class }) 
public interface EventProjection {
  Long getId();
  String getName();
  String getLocality();
  Set<Run> getRuns();
}

