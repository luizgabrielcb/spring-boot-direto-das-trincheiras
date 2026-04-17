package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NameAlreadyExistsException extends ResponseStatusException {
    public NameAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
