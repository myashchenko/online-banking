package io.github.yashchenkon.banking.domain.repository.account;

import io.github.yashchenkon.banking.domain.model.account.Account;

public interface AccountRepository {

    Account accountOfIban(String iban);

    void save(Account account);

    boolean exists(String iban);

    boolean withdraw(String iban, Double amount);

    boolean deposit(String iban, Double amount);
}
