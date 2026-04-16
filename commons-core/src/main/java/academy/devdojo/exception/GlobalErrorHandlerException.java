package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalErrorHandlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handlerNotFoundException(NotFoundException e) {
        var error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handlerSQLIntegrityConstraintViolationException() {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicate entry for one of the unique fields");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<DefaultErrorMessage> handlerEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

