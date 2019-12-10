package io.github.yashchenkon.banking.api.common.exception;

public class ValidationFailedException extends RuntimeException {

    private final String field;
    private final ValidationStatus status;

    public ValidationFailedException(String field, ValidationStatus status) {
        super("Field '" + field + "' " + status.message);

        this.field = field;
        this.status = status;
    }

    public enum ValidationStatus {
        BLANK("shouldn't be blank"),
        INVALID_VALUE("should be a valid value"),
        UNSUPPORTED_VALUE("contains unsupported value");

        private final String message;

        ValidationStatus(String message) {
            this.message = message;
        }
    }
}
