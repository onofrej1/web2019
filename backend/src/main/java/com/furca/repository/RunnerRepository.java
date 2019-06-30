package com.furca.repository;

import com.furca.model.Runner;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface RunnerRepository extends PagingAndSortingRepository<Runner, Integer> {
	
}