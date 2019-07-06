package com.furca.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "MenuItemProjection", types = { MenuItem.class }) 
public interface MenuItemProjection {
	  Long getId();
	  String getTitle();
	  Menu getMenu();
	  Page getPage();
	  MenuItem getParent();
}
