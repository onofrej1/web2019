package com.furca;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan
@Configuration
public class GlobalCorsConfiguration implements WebMvcConfigurer {
 
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**");
    }*/
}