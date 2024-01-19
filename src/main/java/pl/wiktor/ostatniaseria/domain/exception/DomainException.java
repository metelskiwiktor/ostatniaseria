package pl.wiktor.ostatniaseria.domain.exception;

public class DomainException extends RuntimeException {
    private final ErrorCode errorCode;
    public DomainException(ErrorCode errorCode, String... params) {
        super(String.format(errorCode.getMessage(), params));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
