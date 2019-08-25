package com.furca.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(
                entityManager.getMetamodel().getEntities().stream()
                .map(Type::getJavaType)
                .toArray(Class[]::new));
    }
    
    
    
    /*@Bean
    public HateoasPageableHandlerMethodArgumentResolver customResolver(
        HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
        //pageableResolver.setOneIndexedParameters(true);
    	pageableResolver.setOneIndexedParameters(true);
        pageableResolver.setFallbackPageable(PageRequest.of(1, 100000));
        
        
        //pageableResolver.isFallbackPageable(Pageable.unpaged());
        return pageableResolver;
    }*/
    
}