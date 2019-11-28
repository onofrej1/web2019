package com.furca.web;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import com.furca.model.*;
import org.springframework.data.domain.Page;
import com.furca.repository.*;
import com.furca.search.BasicSpecification;
import com.furca.search.CriteriaParser;
import com.furca.search.GenericSpecificationsBuilder;
import com.furca.search.SearchOperation;
import com.furca.search.UserSpecification;
import com.furca.search.UserSpecificationsBuilder;
import com.google.common.base.Joiner;

@RestController
public class ApiController {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private RunRepository runRepo;
	
	@Autowired
	private ApplicationContext context;

	@Autowired
	private RepositoryEntityLinks entityLinks;
		
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		// binder.registerCustomEditor(Book.class, new BookEditor(bookRepo));
	}

	@RequestMapping(value = "/events/{id}/", method = RequestMethod.PATCH)
	public ResponseEntity<Object> updateEvent(@PathVariable("id") long id, @RequestBody Event event) {

		Set<Run> deleteRuns = runRepo.findByEventId(event.getId())
				.stream()
				.filter(run -> !event.getRunIds().contains(run.getId()))
				.collect(Collectors.toSet());

		runRepo.deleteAll(deleteRuns);

		for (Run run : event.getRuns()) {
			run.setEvent(event);
			runRepo.save(run);
		}
		
		eventRepo.save(event);

		return ResponseEntity.ok("success");
	}

	@RequestMapping(value = "/events/", method = RequestMethod.POST)
	public ResponseEntity<Object> createEvent(@RequestBody Event event) {
		Set<Run> runs =  event.getRuns();
		eventRepo.save(event);
		
		for (Run run : runs) {	
			run.setEvent(event);
			runRepo.save(run);
		}		

		return ResponseEntity.ok("success");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/search/{entity}")
	@ResponseBody
	public List<Searchable> searchEntity(@PathVariable("entity") String entity, @RequestParam(value = "search") String search) throws ClassNotFoundException {
		GenericSpecificationsBuilder<Searchable> builder = new GenericSpecificationsBuilder<Searchable>();
	    Specification<Searchable> spec = builder.build(search);
	    
	    String fullPathOfTheClass = "com.furca.repository."+entity+"Repository";	
	    
		@SuppressWarnings("unchecked")
		Class<JpaSpecificationExecutor<Searchable>> cls = (Class<JpaSpecificationExecutor<Searchable>>) Class.forName(fullPathOfTheClass);
		JpaSpecificationExecutor<Searchable> sc = (JpaSpecificationExecutor<Searchable>) context.getBean(cls);
	    
	    List<Searchable> users = sc.findAll(spec);	    
	    //System.out.println(users);
	    
	    return users;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/runs")
	@ResponseBody
	public Page findAllTest(@RequestParam(value = "search") String search) {
		GenericSpecificationsBuilder<User> builder = new GenericSpecificationsBuilder<User>();
	    Specification<User> spec = builder.build(search);
	    
	    Pageable pg = PageRequest.of(0, 3, Sort.by("id"));
	    
	    Page<Run> users = runRepo.findAll(pg);
	    //Page<Run> usersx = (Page<Run>)runRepo.findByEventId(3L, pg);
	    
	    return users;
	}
	
	/*
	@RequestMapping(value = "/api/{model}", method = RequestMethod.GET)
	public ResponseEntity<Object> aaa(@PathVariable("model") String model, Pageable pageable) throws ClassNotFoundException {
		//System.out.println(pageable);
		
		Link link = entityLinks.linkToCollectionResource(Event.class);
		String rel = link.getRel();
		System.out.println(rel);
		System.out.println(link.getHref());
		
		String modelName = model.substring(0, 1).toUpperCase() + model.substring(1);
		String clsName = "com.furca.repository.RunRepository";
		Class<?> cls = Class.forName(clsName);
		
		PagingAndSortingRepository repo = (PagingAndSortingRepository)context.getBean(cls);
		Page data = repo.findAll(PageRequest.of(0, 3));
		//System.out.println(repo.findAll());
						
		return ResponseEntity.ok(data);
	}*/

	
}
