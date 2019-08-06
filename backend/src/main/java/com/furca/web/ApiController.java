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
import org.modelmapper.ModelMapper;
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

import com.furca.dto.RunnerDto;
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
	private ModelMapper modelMapper;

	@RequestMapping("/helloworld")
	public @ResponseBody String greeting() {
		return "Hello World";
	}

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

	@RequestMapping(value = "/checkNames", method = RequestMethod.POST)
	public ResponseEntity<List> saveUsers(@RequestBody RunnerListContainer runnerList) throws Exception {
		System.out.println(runnerList);

		List<RunnerDto> runners = runnerList.getRunners();

		LevenshteinDistance dist = new LevenshteinDistance();

		for (RunnerDto r : runners) {
			Runner runner = runnerRepo.findRunner(r.getLastName().trim(), r.getFirstName().trim(), r.getBirthdate());
			r.setRunnerId(null);
			r.setNames(new ArrayList());
			
			if (runner != null) {
				r.setRunnerId(runner.getId());
			} else {
				
				for (Runner rn : runnerRepo.findAll()) {
					Integer lnameMispell = dist.apply(r.getLastName(), rn.getLastName());
					if (lnameMispell > 2)
						continue;

					Integer fnameMispell = dist.apply(r.getFirstName(), rn.getFirstName());
					if (fnameMispell > 2)
						continue;

					Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
					cal.setTime(r.getBirthdate());
					int year1 = cal.get(Calendar.YEAR);

					cal.setTime(rn.getBirthdate());
					int year2 = cal.get(Calendar.YEAR);

					Integer born = dist.apply(String.valueOf(year1), String.valueOf(year2));

					if (born > 2)
						continue;

					RunnerDto runnerDto = modelMapper.map(rn, RunnerDto.class);
					System.out.println(runnerDto);
					r.addName(runnerDto);
				}

			}

		}
		return ResponseEntity.ok(runners);
	}

	@RequestMapping(value = "/createRunners", method = RequestMethod.POST)
	public ResponseEntity<List> createUsers(@RequestBody RunnerListContainer runnerList) throws Exception {
		System.out.println(runnerList);

		List<RunnerDto> runners = runnerList.getRunners();

		for (RunnerDto runnerDto : runners) {
			
			Runner runner = modelMapper.map(runnerDto, Runner.class);
			runnerRepo.save(runner);
			Long id = runnerRepo.save(runner).getId();
			runnerDto.setRunnerId(id);
			
			System.out.println(id);
		}
		return ResponseEntity.ok(runners);
	}

	/*
	 * @RequestMapping(value = "xxx", method = RequestMethod.POST)
	 * public @ResponseBody String xxx(@RequestParam("book") Book book) {
	 * System.out.println(book.getTitle()); return "ababab"; }
	 */
}
