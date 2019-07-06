package com.furca.repository;

import com.furca.model.Menu;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuRepository extends PagingAndSortingRepository<Menu, Long> {
	
	
}
