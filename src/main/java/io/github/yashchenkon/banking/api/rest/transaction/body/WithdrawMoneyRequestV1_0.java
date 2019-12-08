package io.github.yashchenkon.banking.api.rest.transaction.body;

public class WithdrawMoneyRequestV1_0 {
    private String targetAccountId;
    private Double amount;

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(String targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
