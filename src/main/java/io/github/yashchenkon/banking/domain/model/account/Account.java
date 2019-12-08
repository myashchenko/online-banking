package io.github.yashchenkon.banking.domain.model.account;

import java.util.Currency;

public class Account {

    private Long id;
    private final String iban;
    private final String name;
    private final Currency currency;
    private final Double balance;

    public Account(Long id, String iban, String name, Currency currency, Double balance) {
        this.id = id;
        this.iban = iban;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    public void initId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public String iban() {
        return iban;
    }

    public String name() {
        return name;
    }

    public Currency currency() {
        return currency;
    }

    public Double balance() {
        return balance;
    }
}
