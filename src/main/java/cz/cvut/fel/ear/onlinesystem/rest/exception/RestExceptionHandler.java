package cz.cvut.fel.ear.onlinesystem.rest.exception;


import cz.cvut.fel.ear.onlinesystem.exception.ConflictException;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<RestError> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(404).body(
      new RestError(404, ex.getMessage())
    );
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<RestError> handleValidation(ValidationException ex) {
    return ResponseEntity.status(400).body(
      new RestError(400, ex.getMessage())
    );
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<RestError> handleConflict(ConflictException ex) {
    return ResponseEntity.status(409).body(
      new RestError(409, ex.getMessage())
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestError> handleGeneral(Exception ex) {
    return ResponseEntity.status(500).body(
      new RestError(500, "Unexpected server error")
    );
  }
}
