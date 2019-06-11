package com.furca;

import java.util.Optional;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.furca.model.Event;
import com.furca.repository.EventRepository;

@Component
public class StringToEventConverter
  implements Converter<String, Event> {
	
	public StringToEventConverter(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }
	
	private EventRepository eventRepo;
 
    @Override
    public Event convert(String source) {
    	Optional<Event> event = eventRepo.findById(Integer.parseInt(source));

    	return event.isPresent() ? event.get() : null;
    }
}