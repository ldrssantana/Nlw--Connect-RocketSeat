package br.com.nlw.events.service;


import br.com.nlw.events.model.Event;
import br.com.nlw.events.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired  //injeção de depencias//
    private EventRepo eventRepo;

    public Event addNewEvent(Event event) {
        //gerando o pretty name
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepo.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepo.findAll();

    }

    public  Event getbyPrettyName(String prettyName) {
        return eventRepo.findByPrettyName(prettyName);

    }


}
