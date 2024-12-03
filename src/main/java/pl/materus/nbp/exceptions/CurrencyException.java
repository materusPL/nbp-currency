package pl.materus.nbp.exceptions;

import org.springframework.http.HttpStatus;

public class CurrencyException extends RuntimeException {
    private HttpStatus status;

    public CurrencyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
