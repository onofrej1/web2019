package com.furca.repository;

import com.furca.model.Event;
import com.furca.model.Run;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface RunRepository extends PagingAndSortingRepository<Run, Integer> {
	Set<Run> findByEvent(Event event);
}