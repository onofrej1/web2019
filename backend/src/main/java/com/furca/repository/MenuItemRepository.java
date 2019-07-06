package com.furca.repository;

import com.furca.model.MenuItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, Long> {
	
	
}
