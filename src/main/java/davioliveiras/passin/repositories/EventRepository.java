package davioliveiras.passin.repositories;

import davioliveiras.passin.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {

    Optional<Event> findEventByTitle(String title);
}
