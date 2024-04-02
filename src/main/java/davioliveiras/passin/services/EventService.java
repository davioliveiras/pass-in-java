package davioliveiras.passin.services;

import davioliveiras.passin.domain.attendee.Attendee;
import davioliveiras.passin.domain.event.Event;
import davioliveiras.passin.domain.event.exceptions.EventNotFoudException;
import davioliveiras.passin.dto.event.EventIdDTO;
import davioliveiras.passin.dto.event.EventRequestDTO;
import davioliveiras.passin.dto.event.EventResponseDTO;
import davioliveiras.passin.repositories.AttendeeRepository;
import davioliveiras.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetails(String eventId){
        //findById devolve um Optional
        Event result = this.eventRepository.findById(eventId).orElseThrow(()
                -> new EventNotFoudException("Evento com ID " + eventId + " não encontrado."));

        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);

        return new EventResponseDTO(result, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("[\\s+]", "-")
                .toLowerCase();
    }
}
