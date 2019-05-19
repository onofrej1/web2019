package com.furca.repository;

import com.furca.model.Event;
import com.furca.model.MyEvent;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:8083")
@RepositoryRestResource(excerptProjection = MyEvent.class)
public interface EventRepository extends PagingAndSortingRepository<Event, Integer> {
	
	
}