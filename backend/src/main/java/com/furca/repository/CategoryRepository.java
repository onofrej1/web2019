package com.furca.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.furca.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
	
	
}
