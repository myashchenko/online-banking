package io.github.yashchenkon.banking.domain.factory.account;

import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.service.account.iban.IbanGenerator;

import java.util.Currency;

import javax.inject.Inject;

public class AccountFactory {

    private final IbanGenerator ibanGenerator;

    @Inject
    public AccountFactory(IbanGenerator ibanGenerator) {
        this.ibanGenerator = ibanGenerator;
    }

    public Account create(String name, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);

        return new Account(
            ibanGenerator.generate(), name, currency, 0.0
        );
    }
}
