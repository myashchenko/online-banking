package io.github.yashchenkon.banking.domain.repository.account.h2;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;
import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.h2.transformer.AccountRsTransformer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

public class H2AccountRepository implements AccountRepository {

    private final DataSource dataSource;

    @Inject
    public H2AccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Account accountOfIban(String iban) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE iban = ?")) {
            preparedStatement.setString(1, iban);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return new AccountRsTransformer(resultSet).transform();
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void save(Account account) {

    }
}
