package cz.cvut.fel.ear.onlinesystem.rest.exception;


import cz.cvut.fel.ear.onlinesystem.exception.ConflictException;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

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
    log.error("Unexpected error", ex);
    return ResponseEntity.status(500).body(
      new RestError(500, "Unexpected server error")
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<RestError> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(403).body(
            new RestError(403, "Access denied")
    );
  }
}
