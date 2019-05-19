package com.furca;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furca.model.*;
import com.furca.repository.*;

@Controller
public class TestController {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private RunRepository runRepo;

	@RequestMapping("/aaa")
	public @ResponseBody String greeting() {
		return "Hello World";
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
