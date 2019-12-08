package io.github.yashchenkon.banking.api.rest.account.body;

public class AccountResponseBodyV1_0 {
    private final String iban;
    private final String name;
    private final String currency;
    private final Double balance;

    public AccountResponseBodyV1_0(String iban, String name, String currency, Double balance) {
        this.iban = iban;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    public String getIban() {
        return iban;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }
}
