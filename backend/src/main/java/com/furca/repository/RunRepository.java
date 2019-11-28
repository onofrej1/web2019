package com.furca.repository;

import com.furca.model.Event;
import com.furca.model.RunProjection;
import com.furca.model.Searchable;
import com.furca.model.Run;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection = RunProjection.class)
public interface RunRepository extends PagingAndSortingRepository<Run, Long>, JpaSpecificationExecutor<Searchable> {
		
	Set<Run> findByEvent(Event event);
	
	@RestResource(path = "event", rel = "event")
	public Set<Run> findByEventId(@Param("id") Long id);
	
}