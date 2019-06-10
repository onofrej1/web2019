package com.furca;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.furca.model.*;
import com.furca.repository.*;

@RestController
public class ApiController {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private RunRepository runRepo;

	@Autowired
	private AuthorRepository authorRepo;	

	@RequestMapping("/aaa")
	public @ResponseBody String greeting() {
		return "Hello World";
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		//binder.registerCustomEditor(Book.class, new BookEditor(bookRepo));		
	}

	@RequestMapping(value = "/authors/{id}/", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateAuthor(@PathVariable("id") long id, @RequestBody Author author) {		
		authorRepo.save(author);

		return new ResponseEntity<Object>(HttpStatus.OK);
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
		//for (Run run : event.getRuns()) {
			//runRepo.save(run);
			//run.setEvent(event);
		//}
		//eventRepo.save(event);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value = "xxx", method = RequestMethod.POST)
	 * public @ResponseBody String xxx(@RequestParam("book") Book book) {
	 * System.out.println(book.getTitle()); return "ababab"; }
	 */
}
