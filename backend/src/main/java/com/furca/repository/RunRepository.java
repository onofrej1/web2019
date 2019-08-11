package com.furca.repository;

import com.furca.model.Event;
import com.furca.model.RunProjection;
import com.furca.model.Run;

import java.util.List;
import java.util.Set;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection = RunProjection.class)
public interface RunRepository extends PagingAndSortingRepository<Run, Long> {
		
	Set<Run> findByEvent(Event event);
	
	@RestResource(path = "event", rel = "event")
	public List<Run> findByEventId(@Param("id") Long id);
	
}