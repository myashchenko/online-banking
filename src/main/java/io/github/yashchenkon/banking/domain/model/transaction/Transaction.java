package io.github.yashchenkon.banking.domain.model.transaction;

import java.time.Instant;

public class Transaction {
    private final String id;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final Double amount;
    private final Instant completedAt;

    public Transaction(String id, String sourceAccountId, String targetAccountId, Double amount, Instant completedAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.completedAt = completedAt;
    }

    public String id() {
        return id;
    }

    public String sourceAccountId() {
        return sourceAccountId;
    }

    public String targetAccountId() {
        return targetAccountId;
    }

    public Double amount() {
        return amount;
    }

    public Instant completedAt() {
        return completedAt;
    }
}
