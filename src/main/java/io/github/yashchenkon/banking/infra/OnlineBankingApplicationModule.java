package io.github.yashchenkon.banking.infra;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.h2.jdbcx.JdbcConnectionPool;

import io.github.yashchenkon.banking.api.rest.account.AccountsRestApiV1_0;
import io.github.yashchenkon.banking.api.rest.account.adapter.AccountsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.account.validator.AccountRestApiValidatorV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.TransactionsRestApiV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.adapter.TransactionsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.validator.TransactionsRestApiValidatorV1_0;
import io.github.yashchenkon.banking.domain.factory.account.AccountFactory;
import io.github.yashchenkon.banking.domain.factory.transaction.TransactionFactory;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.h2.H2AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.h2.assembler.AccountAssembler;
import io.github.yashchenkon.banking.domain.repository.transaction.TransactionRepository;
import io.github.yashchenkon.banking.domain.repository.transaction.h2.H2TransactionRepository;
import io.github.yashchenkon.banking.domain.repository.transaction.h2.assembler.TransactionAssembler;
import io.github.yashchenkon.banking.domain.service.account.AccountService;
import io.github.yashchenkon.banking.domain.service.account.iban.IbanGenerator;
import io.github.yashchenkon.banking.domain.service.transaction.TransactionService;
import io.github.yashchenkon.banking.infra.database.ConnectionProvider;
import io.github.yashchenkon.banking.infra.database.TransactionalExecutor;
import io.github.yashchenkon.banking.infra.properties.DatabaseProperties;
import io.github.yashchenkon.banking.infra.properties.PropertiesLoader;

import java.util.Properties;

import javax.sql.DataSource;

public class OnlineBankingApplicationModule extends AbstractModule {

    private static final String APPLICATION_PROPERTIES_LOCATION = "config/application.properties";

    private static final PropertiesLoader PROPERTIES_LOADER = new PropertiesLoader();

    @Override
    protected void configure() {
        bind(Gson.class).in(Singleton.class);

        bind(OnlineBankingApplicationServer.class).in(Singleton.class);

        bind(ConnectionProvider.class).in(Singleton.class);
        bind(TransactionalExecutor.class).in(Singleton.class);

        bind(AccountAssembler.class).in(Singleton.class);
        bind(AccountRepository.class).to(H2AccountRepository.class).in(Singleton.class);
        bind(IbanGenerator.class).in(Singleton.class);
        bind(AccountFactory.class).in(Singleton.class);
        bind(AccountService.class).in(Singleton.class);
        bind(AccountRestApiValidatorV1_0.class).in(Singleton.class);
        bind(AccountsRestApiAdapterV1_0.class).in(Singleton.class);
        bind(AccountsRestApiV1_0.class).in(Singleton.class);

        bind(TransactionAssembler.class).in(Singleton.class);
        bind(TransactionRepository.class).to(H2TransactionRepository.class).in(Singleton.class);
        bind(TransactionFactory.class).in(Singleton.class);
        bind(TransactionService.class).in(Singleton.class);
        bind(TransactionsRestApiValidatorV1_0.class).in(Singleton.class);
        bind(TransactionsRestApiAdapterV1_0.class).in(Singleton.class);
        bind(TransactionsRestApiV1_0.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public DataSource dataSource() {
        Properties applicationProperties = PROPERTIES_LOADER.load(APPLICATION_PROPERTIES_LOCATION);
        DatabaseProperties databaseProperties = new DatabaseProperties(applicationProperties);

        return JdbcConnectionPool.create(databaseProperties.url(), databaseProperties.username(), databaseProperties.password());
    }
}
