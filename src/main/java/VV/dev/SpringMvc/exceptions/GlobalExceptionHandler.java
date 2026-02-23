package VV.dev.SpringMvc.exceptions;

import VV.dev.SpringMvc.exceptions.custom_exceptions.UserEmailAlreadyExistsException;
import VV.dev.SpringMvc.exceptions.custom_exceptions.UserHasPetException;
import VV.dev.SpringMvc.exceptions.custom_exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex) {
        log.error("Got bad request exception", ex);
        var errorResponse = new ErrorResponse(
                "Сущность не найдена",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserEmailAlreadyExists(UserEmailAlreadyExistsException ex) {
        log.error("Got bad request exception", ex);
        var errorResponse = new ErrorResponse(
                "Некорректный запрос",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Got bad request exception", ex);

        String detailedMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorResponse = new ErrorResponse(
                "Некорректный запрос",
                detailedMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundWhenCreatingPet(UserNotFoundException ex) {
        log.error("Got bad request exception", ex);
        var errorResponse = new ErrorResponse(
                "Некорректный запрос",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserHasPetException.class)
    public ResponseEntity<ErrorResponse> handleUserHasPetsException(UserHasPetException ex) {
        log.error("Got bad request exception", ex);
        var errorResponse = new ErrorResponse(
                "Невозможно удалить пользователя",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
