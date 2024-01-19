package pl.wiktor.ostatniaseria.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import pl.wiktor.ostatniaseria.domain.exception.DomainException;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = {DomainException.class})
    //Lets say YourDomainException is your domain exception
    protected ResponseEntity<Object> handleConflict(DomainException ex, WebRequest request) {
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), ex.getErrorCode().getStatus());
    }
}