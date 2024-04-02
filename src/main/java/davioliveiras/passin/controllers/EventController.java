package davioliveiras.passin.controllers;

import davioliveiras.passin.dto.event.EventIdDTO;
import davioliveiras.passin.dto.event.EventRequestDTO;
import davioliveiras.passin.dto.event.EventResponseDTO;
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

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable  String eventId){

        EventResponseDTO event = this.eventService.getEventDetails(eventId);
        return ResponseEntity.ok(event);

    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder UriComponentsBuilder){
        EventIdDTO eventId = this.eventService.createEvent(body);

        var uri = UriComponentsBuilder.path("/event/{id}").buildAndExpand(eventId.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventId);
    }
}
