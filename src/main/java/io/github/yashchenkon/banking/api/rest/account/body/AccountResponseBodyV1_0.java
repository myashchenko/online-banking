package io.github.yashchenkon.banking.api.rest.account.body;

public class AccountResponseBodyV1_0 {
    private final String iban;

    public AccountResponseBodyV1_0(String iban) {
        this.iban = iban;
    }
}
