package io.github.yashchenkon.banking.domain.factory.transaction;

import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.model.transaction.TransactionType;

import java.time.Instant;
import java.util.UUID;

public class TransactionFactory {

    /**
     * Creates new transaction.
     *
     * @param type - type of transaction to be created
     * @param sourceAccountId - source account of transaction
     * @param targetAccountId - target account of transaction
     * @param amount - amount of transaction
     * @return transaction
     */
    public Transaction create(TransactionType type, String sourceAccountId, String targetAccountId, Double amount) {
        return new Transaction(
            UUID.randomUUID().toString(), type, sourceAccountId, targetAccountId, amount, Instant.now()
        );
    }

    /**
     * Creates new transaction.
     *
     * @param type - type of transaction to be created
     * @param targetAccountId - target account of transaction
     * @param amount - amount of transaction
     * @return transaction
     */
    public Transaction create(TransactionType type, String targetAccountId, Double amount) {
        return create(type, targetAccountId, targetAccountId, amount);
    }
}
