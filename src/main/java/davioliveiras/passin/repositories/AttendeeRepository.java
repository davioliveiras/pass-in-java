package davioliveiras.passin.repositories;

import davioliveiras.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    /**
    * @parm eventId => O JPA já entende que precisa buscar os partipipantes pelo id do evento
    * */
    List<Attendee> findByEventId(String eventId);
}
