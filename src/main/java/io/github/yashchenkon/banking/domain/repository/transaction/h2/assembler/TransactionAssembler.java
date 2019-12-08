package io.github.yashchenkon.banking.domain.repository.transaction.h2.assembler;

import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.model.transaction.TransactionType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionAssembler {

    public Transaction assemble(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Transaction(
                resultSet.getString("id"), TransactionType.valueOf(resultSet.getString("type")),
                resultSet.getString("source_account_id"), resultSet.getString("target_account_id"),
                resultSet.getDouble("amount"), resultSet.getTimestamp("completed_at").toInstant()
            );
        }

        return null;
    }
}
