package com.furca.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	private RunnerRepository runnerRepo;	

	@RequestMapping("/helloworld")
	public @ResponseBody String greeting() {
		return "Hello World";
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		//binder.registerCustomEditor(Book.class, new BookEditor(bookRepo));		
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
	
	@RequestMapping(value = "/checkNames", method = RequestMethod.POST)
	public ResponseEntity<List> saveUsers(@RequestBody RunnerListContainer runnerList) throws Exception{
		System.out.println(runnerList);
	    List<Runner> runners = runnerList.getRunners();
	    
	    Map found = new HashMap();
	    Map notFound = new HashMap();
	    Map misspelled = new HashMap();
	    
	    LevenshteinDistance dist = new LevenshteinDistance();
	    
	    for(Runner r : runners) {
	    	Runner runner = runnerRepo.findRunner(r.getLastName().trim(), r.getFirstName().trim(), r.getBirthday());
	    	System.out.println(runner);
	    	if(runner != null) {
	    		//found.put(runner.getId(), runner);
		        //System.out.println("Found " + r.getLastName());
	    	} else {
	    		boolean match = false;
	    		List matches = new ArrayList();
	    		System.out.println("Not Found:"+ r.getLastName()+"-"+r.getFirstName()+"-"+r.getBirthday());
	    		for(Runner rn : runnerRepo.findAll()) {
	    			Integer lnameMispell = dist.apply(r.getLastName(), rn.getLastName());
	    			if(lnameMispell > 2) continue;
	    			
	    			Integer fnameMispell = dist.apply(r.getFirstName(), rn.getFirstName());
	    			if(fnameMispell > 2) continue;
	    			
	    			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
	    			cal.setTime(r.getBirthday());
	    			int year1 = cal.get(Calendar.YEAR);
	    			
	    			cal.setTime(rn.getBirthday());
	    			int year2 = cal.get(Calendar.YEAR);
	    			
	    			Integer born = dist.apply(String.valueOf(year1), String.valueOf(year2));
	    			System.out.println("born"+born);
	    			System.out.println(year1);
	    			System.out.println(year2);
	    			
	    			if(born > 2) continue;
	    				    			
	    			System.out.println("Misspelled " + r.getLastName()+ " "+r.getFirstName()+ " "+r.getBirthday().toString());
	    			matches.add(rn.getLastName()+" "+rn.getFirstName()+ " "+year2);
	    			match = true;
	    		}
	    		if(match) {
	    			misspelled.put(r.getId(), matches);
	    		} else {
	    			notFound.put(r.getId(), null);
	    			//System.out.println("Not found " + r.getLastName());
	    		}
	    	}
	    	
	    }
	    return ResponseEntity.ok(Arrays.asList(found, notFound, misspelled));
	}

	/*
	 * @RequestMapping(value = "xxx", method = RequestMethod.POST)
	 * public @ResponseBody String xxx(@RequestParam("book") Book book) {
	 * System.out.println(book.getTitle()); return "ababab"; }
	 */
}
