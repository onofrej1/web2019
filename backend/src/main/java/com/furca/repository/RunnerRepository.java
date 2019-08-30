package com.furca.repository;

import com.furca.model.Result;
import com.furca.model.Runner;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface RunnerRepository extends PagingAndSortingRepository<Runner, Long> {
	
	@Query("SELECT r FROM Runner r WHERE r.lastName = :lastName and r.firstName = :firstName and r.birthdate = :birthdate")
	Runner findRunner(
	  @Param("lastName") String lastName,
	  @Param("firstName") String firstName,
	  @Param("birthdate") Date birthdate
	 );
	
	
	public Page<Runner> findAll(Pageable pageable);
}