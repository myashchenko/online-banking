package io.github.yashchenkon.banking.domain.service.account;

import com.google.inject.Inject;

import io.github.yashchenkon.banking.domain.exception.AccountNotFoundException;
import io.github.yashchenkon.banking.domain.factory.account.AccountFactory;
import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.service.account.dto.CreateAccountDto;
import io.github.yashchenkon.banking.infra.database.TransactionalExecutor;

public class AccountService {

    private final AccountRepository repository;
    private final AccountFactory accountFactory;
    private final TransactionalExecutor transactionalExecutor;

    @Inject
    public AccountService(AccountRepository repository, AccountFactory accountFactory, TransactionalExecutor transactionalExecutor) {
        this.repository = repository;
        this.accountFactory = accountFactory;
        this.transactionalExecutor = transactionalExecutor;
    }

    /**
     * Creates new account.
     *
     * @param request - API request
     * @return IBAN of newly created account
     */
    public String create(CreateAccountDto request) {
        Account account = accountFactory.create(request.name(), request.currency());

        transactionalExecutor.execute(() -> repository.save(account));

        return account.iban();
    }

    /**
     * Retrieves account by IBAN number
     *
     * @param iban - IBAN number of account
     * @return account
     */
    public Account accountOfId(String iban) {
        Account account = transactionalExecutor.executeReadOnly(() -> repository.accountOfId(iban));
        if (account == null) {
            throw new AccountNotFoundException();
        }
        return account;
    }
}
