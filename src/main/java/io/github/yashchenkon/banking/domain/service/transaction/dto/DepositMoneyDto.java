package io.github.yashchenkon.banking.domain.service.transaction.dto;

public class DepositMoneyDto {
    private final String targetAccountId;
    private final Double amount;

    public DepositMoneyDto(String targetAccountId, Double amount) {
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public String targetAccountId() {
        return targetAccountId;
    }

    public Double amount() {
        return amount;
    }
}
