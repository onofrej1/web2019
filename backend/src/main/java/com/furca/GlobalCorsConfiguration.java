package com.furca;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.furca.repository.BookRepository;

@Configuration
public class GlobalCorsConfiguration implements WebMvcConfigurer {
 
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**");
    }*/
	
	@Autowired
	private BookRepository bookRepo;
	
	@Override
    public void addFormatters(FormatterRegistry registry) {
        //registry.addConverter(new StringToBookConverter(bookRepo));
        //registry.addConverter(new IntegerToBookConverter(bookRepo));
    }
}