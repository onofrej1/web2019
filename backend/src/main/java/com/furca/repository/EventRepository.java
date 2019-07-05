package com.furca.repository;

import com.furca.model.Event;
import com.furca.model.EventProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = EventProjection.class)
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
	
	
}