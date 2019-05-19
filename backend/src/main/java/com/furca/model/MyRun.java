package com.furca.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "myRun", types = { Run.class }) 
interface MyRun {
  String getName();
  int getEdition();
  Event getEvent();
}
