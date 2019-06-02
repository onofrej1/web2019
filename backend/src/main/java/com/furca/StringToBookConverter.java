package com.furca;

import java.util.Optional;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.furca.model.Book;
import com.furca.repository.BookRepository;

@Component
public class StringToBookConverter
  implements Converter<String, Book> {
	
	public StringToBookConverter(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }
	
	private BookRepository bookRepo;
 
    @Override
    public Book convert(String source) {
    	Optional<Book> book = bookRepo.findById(Integer.parseInt(source));
    	if(book.isPresent()) {
    		Book b = book.get();
    		return b;
    	} else {
    		return null;
    	}
    }
}