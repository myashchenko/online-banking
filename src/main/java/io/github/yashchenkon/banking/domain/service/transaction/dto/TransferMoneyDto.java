package io.github.yashchenkon.banking.domain.service.transaction.dto;

public class TransferMoneyDto {
    private final String sourceAccountId;
    private final String targetAccountId;
    private final Double amount;

    public TransferMoneyDto(String sourceAccountId, String targetAccountId, Double amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
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
}
