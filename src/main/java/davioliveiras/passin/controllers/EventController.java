package davioliveiras.passin.controllers;

import davioliveiras.passin.domain.event.Event;
import davioliveiras.passin.dto.attendee.AttendeeRequestDTO;
import davioliveiras.passin.dto.attendee.AttendeesListResponseDTO;
import davioliveiras.passin.dto.event.EventIdDTO;
import davioliveiras.passin.dto.event.EventRequestDTO;
import davioliveiras.passin.dto.event.EventResponseDTO;
import davioliveiras.passin.services.AttendeeService;
import davioliveiras.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable  String eventId){

        EventResponseDTO event = this.eventService.getEventDetails(eventId);
        return ResponseEntity.ok(event);

    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventId = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/event/{id}").buildAndExpand(eventId.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventId);
    }

    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable  String eventId){

        EventResponseDTO event = this.eventService.getEventDetails(eventId);
        AttendeesListResponseDTO attendees = this.attendeeService.getEventsAttendee(eventId);
        return ResponseEntity.ok(attendees);
    }

    @PostMapping("/{eventId}/attendee")
    public ResponseEntity<String> createAttendee(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        String attendeeId = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendee/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();

        return ResponseEntity.created(uri).body(attendeeId);
    }
}
