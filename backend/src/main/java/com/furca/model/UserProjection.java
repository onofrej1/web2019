package com.furca.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "userProjection", types = { User.class }) 
public interface UserProjection {
  Long getId();
  String getUsername();
  String getName();
  String getEmail();
  Set<Role> getRoles();
}

