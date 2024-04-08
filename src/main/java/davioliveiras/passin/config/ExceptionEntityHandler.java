package davioliveiras.passin.config;

import davioliveiras.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import davioliveiras.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import davioliveiras.passin.domain.checkin.exceptions.CheckinAlreadyExistsException;
import davioliveiras.passin.domain.event.exceptions.EventFullException;
import davioliveiras.passin.domain.event.exceptions.EventNotFoudException;
import davioliveiras.passin.domain.event.exceptions.EventTitleAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoudException.class)
    public ResponseEntity handleEventNotFound(EventNotFoudException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExist(AttendeeAlreadyExistException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckinAlreadyExistsException.class)
    public ResponseEntity handleCheckinAlreadyExists(CheckinAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity handleEventFull(EventFullException exception){
        //Podia ter um DTO para essa mensagem
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(EventTitleAlreadyUsedException.class)
    public ResponseEntity handleEventTitleAlreadyUsed(EventTitleAlreadyUsedException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
