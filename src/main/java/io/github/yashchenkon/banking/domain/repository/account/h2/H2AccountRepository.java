package io.github.yashchenkon.banking.domain.repository.account.h2;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;
import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.h2.transformer.AccountRsTransformer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(iban, name, currency, balance) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, account.iban());
            preparedStatement.setString(2, account.name());
            preparedStatement.setString(3, account.currency().getCurrencyCode());
            preparedStatement.setDouble(4, account.balance());

            int result = preparedStatement.executeUpdate();
            if (result == 0) {
                throw new DataAccessException("Can't create account, affected rows size == 0");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.initId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
