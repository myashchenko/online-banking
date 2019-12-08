package io.github.yashchenkon.banking.domain.repository.account.h2.transformer;

import io.github.yashchenkon.banking.domain.model.account.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRsTransformer {

    private final ResultSet resultSet;

    public AccountRsTransformer(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Account transform() throws SQLException {
        if (resultSet.next()) {

        }

        return null;
    }
}
