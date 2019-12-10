package io.github.yashchenkon.banking.domain.repository.account.h2;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;
import io.github.yashchenkon.banking.domain.model.account.Account;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.account.h2.assembler.AccountAssembler;
import io.github.yashchenkon.banking.infra.database.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

/**
 * Implementation of repository storing information in H2 database.
 */
public class H2AccountRepository implements AccountRepository {

    private final ConnectionProvider connectionProvider;
    private final AccountAssembler accountAssembler;

    @Inject
    public H2AccountRepository(ConnectionProvider connectionProvider, AccountAssembler accountAssembler) {
        this.connectionProvider = connectionProvider;
        this.accountAssembler = accountAssembler;
    }

    @Override
    public Account accountOfId(String iban) {
        Connection connection = connectionProvider.acquire();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
            preparedStatement.setString(1, iban);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return accountAssembler.assemble(resultSet);
            }

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void save(Account account) {
        Connection connection = connectionProvider.acquire();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(id, name, currency, balance) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, account.iban());
            preparedStatement.setString(2, account.name());
            preparedStatement.setString(3, account.currency().getCurrencyCode());
            preparedStatement.setDouble(4, account.balance());

            int result = preparedStatement.executeUpdate();
            if (result == 0) {
                throw new DataAccessException("Can't create account, affected rows size == 0");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean exists(String iban) {
        Connection connection = connectionProvider.acquire();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM accounts WHERE id = ? LIMIT 1")) {
            preparedStatement.setString(1, iban);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean withdraw(String iban, Double amount) {
        Connection connection = connectionProvider.acquire();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?")) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, iban);
            preparedStatement.setDouble(3, amount);

            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean deposit(String iban, Double amount) {
        Connection connection = connectionProvider.acquire();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, iban);

            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
