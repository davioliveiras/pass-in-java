package davioliveiras.passin.services;

import davioliveiras.passin.domain.attendee.Attendee;
import davioliveiras.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import davioliveiras.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import davioliveiras.passin.domain.checkin.Checkin;
import davioliveiras.passin.dto.attendee.AttendeeBadgeResponseDTO;
import davioliveiras.passin.dto.attendee.AttendeeDetailDTO;
import davioliveiras.passin.dto.attendee.AttendeesListResponseDTO;
import davioliveiras.passin.dto.attendee.AttendeeBadgeDTO;
import davioliveiras.passin.repositories.AttendeeRepository;
import davioliveiras.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinService checkinService;

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Esse participante já está nesse evento");
    }

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailDTOList = attendeeList.stream().map(attendee -> {
            Optional<Checkin> checkin = this.checkinService.getCheckin(attendee.getId());

            LocalDateTime checkedInAt = checkin.isPresent() ? checkin.get().getCreatedAt() : null;

            return new AttendeeDetailDTO(attendee.getId(), attendee.getName(), attendee.getEmail(),
                    attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailDTOList);
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendee/{attendeeId}/checkin").buildAndExpand(attendeeId).toUri();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri.toString(), attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }

    public void checkinAttendee(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkinService.registerCheckin(attendee);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() ->
                new AttendeeNotFoundException("Participante com ID " + attendeeId + " não encontrado."));
    }
}
