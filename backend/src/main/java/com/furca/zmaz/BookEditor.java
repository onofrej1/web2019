package com.furca.zmaz;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.furca.model.Book;
import com.furca.repository.BookRepository;

public class BookEditor extends PropertyEditorSupport {
	
	public BookEditor(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }
	
	
	private BookRepository bookRepo;
	 
    @Override
    public String getAsText() {
        return ((Book) getValue()).getTitle();
         
    }
     
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    	System.out.println("editor");
    	Optional<Book> book = bookRepo.findById(Integer.parseInt(text));     
        setValue(book.isPresent() ? book.get() : null);
    }
}