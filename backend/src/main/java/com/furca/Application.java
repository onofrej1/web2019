package com.furca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
    	/*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    	ctx.register(GlobalCorsConfiguration.class);
    	ctx.refresh();*/
    	   
        SpringApplication.run(Application.class, args);
    }
    
    
    
}
