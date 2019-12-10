package io.github.yashchenkon.banking.domain.model.account;

import java.util.Currency;

/**
 * Domain object representing account.
 */
public class Account {

    private final String iban;
    private final String name;
    private final Currency currency;
    private final Double balance;

    public Account(String iban, String name, Currency currency, Double balance) {
        this.iban = iban;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
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
