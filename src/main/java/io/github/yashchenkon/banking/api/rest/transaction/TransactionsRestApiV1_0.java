package io.github.yashchenkon.banking.api.rest.transaction;

import com.google.gson.Gson;

import io.github.yashchenkon.banking.api.rest.transaction.adapter.TransactionsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.ProcessTransactionRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.validator.TransactionsRestApiValidatorV1_0;
import spark.Route;

import javax.inject.Inject;

public class TransactionsRestApiV1_0 {

    private final TransactionsRestApiAdapterV1_0 adapter;
    private final TransactionsRestApiValidatorV1_0 validator;
    private final Gson gson;

    @Inject
    public TransactionsRestApiV1_0(TransactionsRestApiAdapterV1_0 adapter, TransactionsRestApiValidatorV1_0 validator, Gson gson) {
        this.adapter = adapter;
        this.validator = validator;
        this.gson = gson;
    }

    public Route create() {
        return (request, response) -> {
            ProcessTransactionRequestV1_0 requestBody = gson.fromJson(request.body(), ProcessTransactionRequestV1_0.class);
            validator.validate(requestBody);
            return adapter.process(requestBody);
        };
    }
}
