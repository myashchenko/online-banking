package io.github.yashchenkon.banking.domain.repository.transaction.h2;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;
import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.repository.transaction.TransactionRepository;
import io.github.yashchenkon.banking.domain.repository.transaction.h2.assembler.TransactionAssembler;
import io.github.yashchenkon.banking.infra.database.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.inject.Inject;

public class H2TransactionRepository implements TransactionRepository {

    private final ConnectionProvider connectionProvider;
    private final TransactionAssembler transactionAssembler;

    @Inject
    public H2TransactionRepository(ConnectionProvider connectionProvider, TransactionAssembler transactionAssembler) {
        this.connectionProvider = connectionProvider;
        this.transactionAssembler = transactionAssembler;
    }

    @Override
    public void save(Transaction transaction) {
        Connection connection = connectionProvider.acquire();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions(id, source_account_id, target_account_id, amount, completed_at, type) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, transaction.id());
            preparedStatement.setString(2, transaction.sourceAccountId());
            preparedStatement.setString(3, transaction.targetAccountId());
            preparedStatement.setDouble(4, transaction.amount());
            preparedStatement.setTimestamp(5, Timestamp.from(transaction.completedAt()));
            preparedStatement.setString(6, transaction.type().name());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new DataAccessException("Can't insert transaction, updatedRows == 0");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Transaction transactionOfId(String id) {
        Connection connection = connectionProvider.acquire();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transactions WHERE id = ?")) {
            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return transactionAssembler.assemble(resultSet);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
