package io.github.yashchenkon.banking.api.common.body;

public class ErrorResponseBodyV1_0 {
    private final String message;

    public ErrorResponseBodyV1_0(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
