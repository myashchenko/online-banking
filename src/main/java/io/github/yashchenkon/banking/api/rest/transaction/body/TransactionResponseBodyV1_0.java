package io.github.yashchenkon.banking.api.rest.transaction.body;

import java.time.Instant;

public class TransactionResponseBodyV1_0 {
    private final String id;
    private final String type;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final Double amount;
    private final Instant completedAt;

    public TransactionResponseBodyV1_0(String id, String type, String sourceAccountId, String targetAccountId, Double amount, Instant completedAt) {
        this.id = id;
        this.type = type;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
