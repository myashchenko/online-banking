package io.github.yashchenkon.banking.api.rest.account.adapter;

import io.github.yashchenkon.banking.api.rest.account.body.AccountResponseBodyV1_0;
import io.github.yashchenkon.banking.api.rest.account.body.CreateAccountRequestV1_0;
import io.github.yashchenkon.banking.api.common.body.EntityCreatedResponseV1_0;
import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.service.account.AccountService;
import io.github.yashchenkon.banking.domain.service.account.dto.CreateAccountDto;

import javax.inject.Inject;

/**
 * Adapter from REST API to domain services.
 */
public class AccountsRestApiAdapterV1_0 {

    private final AccountService accountService;

    @Inject
    public AccountsRestApiAdapterV1_0(AccountService accountService) {
        this.accountService = accountService;
    }

    public EntityCreatedResponseV1_0 create(CreateAccountRequestV1_0 request) {
        String iban = accountService.create(
            new CreateAccountDto(request.getName(), request.getCurrency())
        );

        return new EntityCreatedResponseV1_0(iban);
    }

    public AccountResponseBodyV1_0 accountOfId(String iban) {
        Account account = accountService.accountOfId(iban);
        return new AccountResponseBodyV1_0(
            account.iban(), account.name(), account.currency().getCurrencyCode(), account.balance()
        );
    }
}
