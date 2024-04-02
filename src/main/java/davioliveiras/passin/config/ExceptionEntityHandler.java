package davioliveiras.passin.config;

import davioliveiras.passin.domain.event.exceptions.EventNotFoudException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoudException.class)
    public ResponseEntity handleEventNotFound(EventNotFoudException exception){
        return ResponseEntity.notFound().build();
    }
}
