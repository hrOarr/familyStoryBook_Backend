package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.EventDTO;
import com.astrodust.familyStoryBook_backend.model.Event;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    private final ModelMapper modelMapper;

    public EventMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public Event toEntity(EventDTO eventDTO, FamilyAccount familyAccount) {
        Event event = modelMapper.map(eventDTO, Event.class);
        if(eventDTO.getId()<=0) { // insertion
            event.setCreatedDate(LocalDateTime.now());
            event.setFamilyAccount(familyAccount);
        }
        else { // updation
            event.setUpdatedDate(LocalDateTime.now());
            event.setFamilyAccount(familyAccount);
        }
        return event;
    }
}
