package io.github.yashchenkon.banking.api.rest.transaction;

import com.google.gson.Gson;

import io.github.yashchenkon.banking.api.rest.transaction.adapter.TransactionsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.DepositMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.TransferMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.WithdrawMoneyRequestV1_0;
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

    public Route transfer() {
        return (request, response) -> {
            TransferMoneyRequestV1_0 requestBody = gson.fromJson(request.body(), TransferMoneyRequestV1_0.class);
            validator.validate(requestBody);
            return adapter.transfer(requestBody);
        };
    }

    public Route deposit() {
        return (request, response) -> {
            DepositMoneyRequestV1_0 requestBody = gson.fromJson(request.body(), DepositMoneyRequestV1_0.class);
            validator.validate(requestBody);
            return adapter.deposit(requestBody);
        };
    }

    public Route withdraw() {
        return (request, response) -> {
            WithdrawMoneyRequestV1_0 requestBody = gson.fromJson(request.body(), WithdrawMoneyRequestV1_0.class);
            validator.validate(requestBody);
            return adapter.withdraw(requestBody);
        };
    }

    public Route getById() {
        return (request, response) -> {
            String transactionId = request.params(":id");
            return adapter.transactionOfId(transactionId);
        };
    }
}
