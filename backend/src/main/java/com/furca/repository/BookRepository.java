package com.furca.repository;

import com.furca.model.Book;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
	List<Book> findByTitle(String title);
	//List<Book> findByIdIn(List<Integer> ids);
}