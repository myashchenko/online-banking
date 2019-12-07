package io.github.yashchenkon.banking.domain.repository.account;

import io.github.yashchenkon.banking.domain.model.account.Account;

public class H2AccountRepository implements AccountRepository {
    @Override
    public Account accountOfIban(String iban) {
        return null;
    }

    @Override
    public void save(Account account) {

    }
}
