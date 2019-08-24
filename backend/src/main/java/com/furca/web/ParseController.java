package com.furca.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.furca.dto.ResultDto;
import com.furca.dto.RunnerDto;
import com.furca.model.*;
import com.furca.repository.*;

@RestController
public class ParseController {

	@Autowired
	private RunnerRepository runnerRepo;
	
	@Autowired
	private RunRepository runRepo;
	
	@Autowired
	private ResultRepository resultRepo;

	@Autowired
	private ModelMapper modelMapper;

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

	@RequestMapping(value = "/createResults", method = RequestMethod.POST)
	public ResponseEntity<List> createResults(@RequestBody List<ResultDto> results) throws Exception {

		Optional<Run> run = runRepo.findById(46L);
		if(!run.isPresent()) {
			throw new Exception("Run not found");
		}

		TypeMap<ResultDto, Result> typeMap = modelMapper.getTypeMap(ResultDto.class, Result.class);
		if(typeMap == null) {
			typeMap = modelMapper.createTypeMap(ResultDto.class, Result.class);
			modelMapper.addMappings(new ResultPropertyMap());
		}
		
		for (ResultDto resultDto : results) {
			Optional<Runner> runner = runnerRepo.findById(resultDto.getRunnerId());			
			Result result = modelMapper.map(resultDto, Result.class);
			
			System.out.println(result);
			
			if(!runner.isPresent()) {
				throw new Exception("Runner not found");
			}
			result.setRunner(runner.get());
			result.setRun(run.get());
			
			resultRepo.save(result);
		}
		return ResponseEntity.ok(results);
	}
	
	class ResultPropertyMap
	    extends PropertyMap<ResultDto, Result> {
			@Override
			protected void configure() {
			    map(source.getPlace(), destination.getPlace());
			    map(source.getCategory(), destination.getCategory());
			    map(source.getTeam(), destination.getTeam());
			    map(source.getFinishTime(), destination.getFinishTime()); 
			    map(source.getRunningNumber(), destination.getRunningNumber());
			    map().setRunner(null);
			    map().setId(null); 
			}
	}
	
	@RequestMapping(value = "/createRunners", method = RequestMethod.POST)
	public ResponseEntity<List> createRunners(@RequestBody RunnerListContainer runnerList) throws Exception {
		System.out.println(runnerList);

		List<RunnerDto> runners = runnerList.getRunners();

		for (RunnerDto runnerDto : runners) {		
			Runner runner = modelMapper.map(runnerDto, Runner.class);
			runnerRepo.save(runner);
			Long id = runnerRepo.save(runner).getId();
			runnerDto.setRunnerId(id);			
		}
		return ResponseEntity.ok(runners);
	}

	
}
