package davioliveiras.passin.controllers;

import davioliveiras.passin.dto.attendee.AttendeeBadgeDTO;
import davioliveiras.passin.dto.attendee.AttendeeBadgeResponseDTO;
import davioliveiras.passin.services.AttendeeService;
import davioliveiras.passin.services.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendee")
public class AttendeeController {
    private final AttendeeService attendeeService;

    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBadgeResponseDTO response = this.attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        return  ResponseEntity.ok(response);
    }

    @PostMapping("/{attendeeId}/checkin")
    public ResponseEntity registerCheckin(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        this.attendeeService.checkinAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendee/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();

        return ResponseEntity.created(uri).build();
    }
}
