package io.github.yashchenkon.banking.api.rest.account;

import com.google.gson.Gson;

import io.github.yashchenkon.banking.api.rest.account.adapter.AccountsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.account.body.CreateAccountRequestV1_0;
import io.github.yashchenkon.banking.api.rest.account.validator.AccountRestApiValidatorV1_0;
import spark.Route;

import javax.inject.Inject;

public class AccountsRestApiV1_0 {

    private final Gson gson;
    private final AccountsRestApiAdapterV1_0 adapter;
    private final AccountRestApiValidatorV1_0 validator;

    @Inject
    public AccountsRestApiV1_0(Gson gson, AccountsRestApiAdapterV1_0 adapter, AccountRestApiValidatorV1_0 validator) {
        this.gson = gson;
        this.adapter = adapter;
        this.validator = validator;
    }

    public Route create() {
        return (request, response) -> {
            CreateAccountRequestV1_0 requestBody = gson.fromJson(request.body(), CreateAccountRequestV1_0.class);
            validator.validate(requestBody);
            return adapter.create(requestBody);
        };
    }

    public Route getById() {
        return (request, response) -> {
            String iban = request.params(":id");
            return adapter.accountOfId(iban);
        };
    }
}
