package io.github.yashchenkon.banking.infra;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import io.github.yashchenkon.banking.api.rest.account.AccountsRestApiV1_0;
import io.github.yashchenkon.banking.api.rest.account.adapter.AccountsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.account.validator.AccountRestApiValidatorV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.TransactionsRestApiV1_0;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.H2AccountRepository;
import io.github.yashchenkon.banking.domain.service.account.AccountService;

public class OnlineBankingApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OnlineBankingApplicationServer.class).in(Singleton.class);

        bind(AccountRepository.class).to(H2AccountRepository.class).in(Singleton.class);
        bind(AccountService.class).in(Singleton.class);
        bind(AccountRestApiValidatorV1_0.class).in(Singleton.class);
        bind(AccountsRestApiAdapterV1_0.class).in(Singleton.class);

        bind(AccountsRestApiV1_0.class).in(Singleton.class);
        bind(TransactionsRestApiV1_0.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    private Gson gson() {
        return new Gson();
    }
}
