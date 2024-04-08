package davioliveiras.passin.domain.event.exceptions;

public class EventTitleAlreadyUsedException extends RuntimeException{

    public EventTitleAlreadyUsedException(String message){
        super(message);
    }
}
