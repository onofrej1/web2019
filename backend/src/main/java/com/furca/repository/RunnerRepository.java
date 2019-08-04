package com.furca.repository;

import com.furca.model.Runner;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface RunnerRepository extends PagingAndSortingRepository<Runner, Long> {
	
	@Query("SELECT r FROM Runner r WHERE r.lastName = :lastName and r.firstName = :firstName and r.birthdate = :birthdate")
	Runner findRunner(
	  @Param("lastName") String lastName,
	  @Param("firstName") String firstName,
	  @Param("birthdate") Date birthdate
	 );
}