package davioliveiras.passin.services;

import davioliveiras.passin.domain.attendee.Attendee;
import davioliveiras.passin.domain.checkin.Checkin;
import davioliveiras.passin.domain.checkin.exceptions.CheckinAlreadyExistsException;
import davioliveiras.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckinService {
    private final CheckinRepository checkinRepository;

    public void registerCheckin(Attendee attendee){
        this.verifyCheckinExists(attendee.getId());
        Checkin checkin = new Checkin();
        checkin.setAttendee(attendee);
        checkin.setCreatedAt(LocalDateTime.now());

        this.checkinRepository.save(checkin);
    }

    public Optional<Checkin> getCheckin(String attendeeId){
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }

    private void verifyCheckinExists(String attendId){
        Optional<Checkin> isCheckedin = this.getCheckin(attendId);
        if(isCheckedin.isPresent()) throw new CheckinAlreadyExistsException("O participante já fez o checkin.");
    }
}
