package com.furca;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.furca.repository.BookRepository;
import com.furca.repository.EventRepository;

@Configuration
public class GlobalCorsConfiguration implements WebMvcConfigurer {
 
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**");
    }*/
	
	@Autowired
	private EventRepository eventRepo;
	
	@Override
    public void addFormatters(FormatterRegistry registry) {
        //registry.addConverter(new StringToEventConverter(eventRepo));
        //registry.addConverter(new IntegerToBookConverter(bookRepo));
    }
}