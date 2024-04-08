package davioliveiras.passin.services;

import davioliveiras.passin.domain.event.Event;
import davioliveiras.passin.domain.event.exceptions.EventTitleAlreadyUsedException;
import davioliveiras.passin.dto.event.EventRequestDTO;
import davioliveiras.passin.repositories.EventRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private AttendeeService attendeeService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("should create an event")
    void createEventCase1() {

    }

    @Test
    @DisplayName("shouldn't create an event with a title that already exists")
    void createEventCase2() {
        Event alreadyExists = new Event(UUID.randomUUID().toString(),
                "NLW Unite", "Evento online.","nlw-unite", 10);

        Optional<Event> ev = Optional.of(alreadyExists);

        when(this.eventService.getEventByTitle("NLW Unite")).thenReturn(ev);
        //when(this.eventService.verifyTitleAlreadyExists("NLW Unite")).thenThrow(new EventTitleAlreadyUsedException("ID já usado"));

        EventRequestDTO eventRequest = new EventRequestDTO("Rocketseat NWL 2", "Aprenda Java Spring Boot", 20);
        //eventService.createEvent(eventRequest);
        eventService.getEventByTitle("NLW Unit");


        //verify(eventRepository, times(1)).findById(any());
    }

    @Test
    void registerAttendeeOnEvent() {

    }
}