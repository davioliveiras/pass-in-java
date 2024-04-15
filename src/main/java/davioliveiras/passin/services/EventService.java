package davioliveiras.passin.services;

import davioliveiras.passin.domain.attendee.Attendee;
import davioliveiras.passin.domain.event.Event;
import davioliveiras.passin.domain.event.exceptions.EventFullException;
import davioliveiras.passin.domain.event.exceptions.EventNotFoudException;
import davioliveiras.passin.domain.event.exceptions.EventTitleAlreadyUsedException;
import davioliveiras.passin.dto.attendee.AttendeeRequestDTO;
import davioliveiras.passin.dto.email.EmailRequest;
import davioliveiras.passin.dto.event.EventIdDTO;
import davioliveiras.passin.dto.event.EventRequestDTO;
import davioliveiras.passin.dto.event.EventResponseDTO;
import davioliveiras.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    @Autowired
    private final EmailServiceClient emailServiceClient;

    public EventResponseDTO getEventDetails(String eventId){
        Event result = this.getEventById(eventId);

        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        return new EventResponseDTO(result, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        this.verifyTitleAlreadyExists(eventDTO.title());

        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public void verifyTitleAlreadyExists(String title){
        Optional<Event> event = this.getEventByTitle(title);
        if(event.isPresent()) throw new EventTitleAlreadyUsedException("Esse título já é usado.");
    }

    public Optional<Event> getEventByTitle(String title){
        return this.eventRepository.findEventByTitle(title);
    }

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("[\\s+]", "-")
                .toLowerCase();
    }

    public String registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);

        Event event = this.getEventById(eventId);

        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if (event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("O evento já está cheio.");

        Attendee attendee = new Attendee();
        attendee.setName(attendeeRequestDTO.name());
        attendee.setEmail(attendeeRequestDTO.email());
        attendee.setEvent(event);
        attendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(attendee);

        EmailRequest request = new EmailRequest(attendee.getEmail(), "Confirmação de inscrição",
                "Você foi cadastrado no evento " + event.getTitle() + ". Não se esqueça de fazer check-in.");
        emailServiceClient.sendEmail(request);

        //Podia criar um DTO
        return attendee.getId();
    }

    private Event getEventById(String eventId){
        //findById devolve um Optional
        return this.eventRepository.findById(eventId).orElseThrow(()
                -> new EventNotFoudException("Evento com ID " + eventId + " não encontrado."));
    }
}
