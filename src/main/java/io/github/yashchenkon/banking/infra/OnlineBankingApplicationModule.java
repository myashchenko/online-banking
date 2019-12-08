package io.github.yashchenkon.banking.infra;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.h2.jdbcx.JdbcDataSource;

import io.github.yashchenkon.banking.api.rest.account.AccountsRestApiV1_0;
import io.github.yashchenkon.banking.api.rest.account.adapter.AccountsRestApiAdapterV1_0;
import io.github.yashchenkon.banking.api.rest.account.validator.AccountRestApiValidatorV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.TransactionsRestApiV1_0;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.h2.H2AccountRepository;
import io.github.yashchenkon.banking.domain.service.account.AccountService;
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

        bind(AccountRepository.class).to(H2AccountRepository.class).in(Singleton.class);
        bind(AccountService.class).in(Singleton.class);
        bind(AccountRestApiValidatorV1_0.class).in(Singleton.class);
        bind(AccountsRestApiAdapterV1_0.class).in(Singleton.class);

        bind(AccountsRestApiV1_0.class).in(Singleton.class);
        bind(TransactionsRestApiV1_0.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public DataSource dataSource() {
        Properties applicationProperties = PROPERTIES_LOADER.load(APPLICATION_PROPERTIES_LOCATION);
        DatabaseProperties databaseProperties = new DatabaseProperties(applicationProperties);

        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl(databaseProperties.url());
        jdbcDataSource.setUser(databaseProperties.username());
        jdbcDataSource.setPassword(databaseProperties.password());

        return jdbcDataSource;
    }
}
