package com.furca.repository;

import com.furca.model.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {
	List<Book> findByTitle(String title);
	//List<Book> findByIdIn(List<Integer> ids);
}