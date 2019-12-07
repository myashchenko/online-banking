package io.github.yashchenkon.banking.domain.service.account;

import com.google.inject.Inject;

import io.github.yashchenkon.banking.domain.exception.AccountNotFoundException;
import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.service.account.dto.CreateAccountDto;

import java.util.Currency;
import java.util.UUID;

public class AccountService {

    private final AccountRepository repository;

    @Inject
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates new account.
     *
     * @param request - API request
     * @return IBAN of newly created account
     */
    public String create(CreateAccountDto request) {
        String iban = UUID.randomUUID().toString();
        Currency currency = Currency.getInstance(request.currency());
        Account account = new Account(null, iban, request.name(), currency, 0.0);

        repository.save(account);

        return account.iban();
    }

    /**
     * Retrieves account by IBAN number
     *
     * @param iban - IBAN number of account
     * @return account
     */
    public Account accountOfIban(String iban) {
        Account account = repository.accountOfIban(iban);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        return account;
    }
}
