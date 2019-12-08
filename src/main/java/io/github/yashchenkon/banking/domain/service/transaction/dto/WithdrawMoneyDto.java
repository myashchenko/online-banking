package io.github.yashchenkon.banking.domain.service.transaction.dto;

public class WithdrawMoneyDto {
    private final String targetAccountId;
    private final Double amount;

    public WithdrawMoneyDto(String targetAccountId, Double amount) {
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
