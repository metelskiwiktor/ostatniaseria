package pl.wiktor.ostatniaseria.domain.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_PASSWORD_OR_EMAIL_NOT_FOUND("Provided password for email '%s' is invalid or account isn't registered", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_REGISTERED("Email '%s' is already registered", HttpStatus.BAD_REQUEST),
    EMAIL_SYNTAX_ERROR("Email '%s' is incorrect", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD("Provided password is weak", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Invalid token '%s'", HttpStatus.INTERNAL_SERVER_ERROR);
    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
