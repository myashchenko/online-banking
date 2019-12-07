package io.github.yashchenkon.banking.domain.repository.account;

import io.github.yashchenkon.banking.domain.model.account.Account;

public interface AccountRepository {

    Account accountOfIban(String iban);

    void save(Account account);
}
