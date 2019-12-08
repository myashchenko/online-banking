package io.github.yashchenkon.banking.domain.factory.transaction;

import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.model.transaction.TransactionType;

import java.time.Instant;
import java.util.UUID;

public class TransactionFactory {

    public Transaction create(TransactionType type, String sourceAccountId, String targetAccountId, Double amount) {
        return new Transaction(
            UUID.randomUUID().toString(), type, sourceAccountId, targetAccountId, amount, Instant.now()
        );
    }

    public Transaction create(TransactionType type, String targetAccountId, Double amount) {
        return create(type, targetAccountId, targetAccountId, amount);
    }
}
