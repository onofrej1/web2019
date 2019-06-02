package com.furca;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furca.model.*;
import com.furca.repository.*;

@RestController
public class TestController {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private RunRepository runRepo;
	
	@Autowired
	private AuthorRepository authorRepo;
	
	@Autowired
	private BookRepository bookRepo;

	@RequestMapping("/aaa")
	public @ResponseBody String greeting() {
		return "Hello World";
	}
	
	@InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
		
	 binder.registerCustomEditor(Book.class,new BookEditor(bookRepo));
	 
     binder.registerCustomEditor(Set.class, "books", new CustomCollectionEditor(Set.class) {

            protected Object convertElement(Object element) {
            	System.out.println("abcd");
                if (element != null) {
                    Integer benefitId = Integer.parseInt(element.toString());
                    Optional<Book> bookOptional = bookRepo.findById(benefitId); // something that able to get benefit object
                    Book model = bookOptional.isPresent() ? bookOptional.get() : new Book();
                    System.out.println(model.getTitle());
                    return model;
                }
                return null;
            }

        });

    }
	
	@RequestMapping(value = "/authors/{id}/", method = RequestMethod.PUT)
	public ResponseEntity updateAuthor(@PathVariable("id") long id, 
			@RequestBody String params) throws IOException
			 {
		//Set<Book> existingBooks = (Set<Book>) bookRepo.findByIdIn(ids);
		//System.out.println(existingBooks);
		/*final Iterator<Entry<String, List<String>>> it = params.entrySet().iterator();
		List<String> values = null;
	    while(it.hasNext()) {
	        final String k = it.next().getKey();
	        values = it.next().getValue();
	        System.out.println(values);
	    }*/
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(params);
	    List<JsonNode> ids = actualObj.findValues("books");
	    System.out.println(actualObj.get("books"));
		System.out.println(params);
		System.out.println(ids);
		//String itemIds = params.get("books");
	      /*for (String itemId : itemIds) {
	          System.out.println("item from map with String id " + itemId + "<br/>");
	      }*/
		
		
		/*List<Run> runsToRemove = existingRuns.stream()
				.filter(r -> runs.stream().filter(rr -> rr.getId() == r.getId()).count() == 0)
				.collect(Collectors.toList());

		runRepo.deleteAll(runsToRemove);

		for (Run run : runs) {
			Optional<Run> modelOptional = runRepo.findById(run.getId());
			Run model = modelOptional.isPresent() ? modelOptional.get() : new Run();
			BeanUtils.copyProperties(model, run);
			model.setEvent(event);

			runRepo.save(model);
		}*/
		return new ResponseEntity(null, HttpStatus.OK);
	}
	
	@RequestMapping(value = "xxx", method = RequestMethod.POST)
	public @ResponseBody String xxx(@RequestParam("book") Book book) {
		System.out.println(book.getTitle());
		return "ababab";
	}

	@RequestMapping(value = "/events/{id}/", method = RequestMethod.PUT)
	public ResponseEntity updateEventWithRuns(@PathVariable("id") long id, @RequestBody Event event)
			throws ParseException, IllegalAccessException, InvocationTargetException {
		Set<Run> existingRuns = (Set<Run>) runRepo.findByEvent(event);
		Set<Run> runs = (Set<Run>) event.getRuns();
		eventRepo.save(event);

		List<Run> runsToRemove = existingRuns.stream()
				.filter(r -> runs.stream().filter(rr -> rr.getId() == r.getId()).count() == 0)
				.collect(Collectors.toList());

		runRepo.deleteAll(runsToRemove);

		for (Run run : runs) {
			Optional<Run> modelOptional = runRepo.findById(run.getId());
			Run model = modelOptional.isPresent() ? modelOptional.get() : new Run();
			BeanUtils.copyProperties(model, run);
			model.setEvent(event);

			runRepo.save(model);
		}
		return new ResponseEntity(event, HttpStatus.OK);
	}

	@RequestMapping(value = "/events/", method = RequestMethod.POST)
	public ResponseEntity saveEventWithRuns(@RequestBody Event event)
			throws ParseException, IllegalAccessException, InvocationTargetException {
		eventRepo.save(event);

		for (Run run : event.getRuns()) {
			Run model = new Run();
			BeanUtils.copyProperties(model, run);
			model.setEvent(event);

			runRepo.save(model);
		}
		return new ResponseEntity(event, HttpStatus.OK);
	}
}
