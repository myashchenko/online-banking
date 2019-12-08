package io.github.yashchenkon.banking.domain.exception;

public class DataAccessException extends RuntimeException {

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
