package com.furca.repository;

import com.furca.model.MenuItemProjection;
import com.furca.model.MenuItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = MenuItemProjection.class)
public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, Long> {
	
	
}
