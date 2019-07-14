package com.furca.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.furca.model.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
	
	
}
