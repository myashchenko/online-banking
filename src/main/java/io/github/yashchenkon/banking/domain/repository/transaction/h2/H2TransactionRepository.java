package io.github.yashchenkon.banking.domain.repository.transaction.h2;

import io.github.yashchenkon.banking.domain.exception.DataAccessException;
import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.repository.transaction.TransactionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.inject.Inject;
import javax.sql.DataSource;

public class H2TransactionRepository implements TransactionRepository {

    private final DataSource dataSource;

    @Inject
    public H2TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Transaction transaction) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions(id, source_account_id, target_account_id, amount, completed_at) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, transaction.id());
            preparedStatement.setString(2, transaction.sourceAccountId());
            preparedStatement.setString(3, transaction.targetAccountId());
            preparedStatement.setDouble(4, transaction.amount());
            preparedStatement.setTimestamp(5, Timestamp.from(transaction.completedAt()));

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new DataAccessException("Can't insert transaction, updatedRows == 0");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
