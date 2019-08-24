package com.furca.web;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
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
import com.furca.repository.*;

@RestController
public class ApiController {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private RunRepository runRepo;
	
	@Autowired
	private RunnerRepository runnerRepo;

	@Autowired
	private ApplicationContext context;

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		// binder.registerCustomEditor(Book.class, new BookEditor(bookRepo));
	}

	@RequestMapping(value = "/events/{id}/", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateEvent(@PathVariable("id") long id, @RequestBody Event event) {

		Set<Run> deleteRuns = runRepo.findByEvent(event).stream()
				.filter(run -> !event.getRunIds().contains(run.getId())).collect(Collectors.toSet());

		runRepo.deleteAll(deleteRuns);

		for (Run run : event.getRuns()) {
			run.setEvent(event);
		}

		eventRepo.save(event);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/events/", method = RequestMethod.POST)
	public ResponseEntity<Object> createEvent(@RequestBody Event event) {
		System.out.println(event);
		for (Run run : event.getRuns()) {
			runRepo.save(run);
			run.setEvent(event);
		}
		eventRepo.save(event);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/apixx/{model}", method=RequestMethod.GET)
	@ResponseBody 
	public Page<Runner> contactsPages(@PathVariable(value="model") String model, Pageable pageable) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		//Pageable pageable = size != null ? PageRequest.of(page, size) : Pageable.unpaged();
		System.out.println(pageable);
		String fullPathOfTheClass = "com.furca.repository.RunnerRepository";
		
		Class cls = Class.forName(fullPathOfTheClass);
		PagingAndSortingRepository sc = (PagingAndSortingRepository)context.getBean(cls);
		System.out.println(pageable.isPaged());
		System.out.println(pageable.next());
	    Page<Runner> pageResult = sc.findAll(pageable);

	    return pageResult;
	} 

	
}
