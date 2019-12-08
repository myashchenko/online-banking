package io.github.yashchenkon.banking.infra.exception;

import com.google.gson.Gson;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yashchenkon.banking.api.common.body.ErrorResponseBodyV1_0;
import io.github.yashchenkon.banking.domain.exception.AccountNotFoundException;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Optional;

public class OnlineBankingApplicationExceptionHandler implements ExceptionHandler<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineBankingApplicationExceptionHandler.class);

    private static Map<Class<? extends Exception>, HttpStatusCodeWithMessage> EXCEPTION_TO_STATUS_MAPPING = Map.of(
        AccountNotFoundException.class, new HttpStatusCodeWithMessage(HttpStatus.NOT_FOUND_404, "Cannot find such account"),
        Exception.class, new HttpStatusCodeWithMessage(HttpStatus.INTERNAL_SERVER_ERROR_500, "General error")
    );

    private final Gson gson;

    public OnlineBankingApplicationExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void handle(Exception exception, Request request, Response response) {
        LOGGER.error("Exception while processing request", exception);

        HttpStatusCodeWithMessage statusCodeWithMessage = Optional.ofNullable(EXCEPTION_TO_STATUS_MAPPING.get(exception.getClass()))
            .orElseGet(() -> EXCEPTION_TO_STATUS_MAPPING.get(Exception.class));

        String responseBody = gson.toJson(new ErrorResponseBodyV1_0(statusCodeWithMessage.message));

        response.body(responseBody);
        response.status(statusCodeWithMessage.code);
    }

    private static class HttpStatusCodeWithMessage {
        private final int code;
        private final String message;

        private HttpStatusCodeWithMessage(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
