package io.github.yashchenkon.banking.domain.repository.account.h2.assembler;

import io.github.yashchenkon.banking.domain.model.account.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;

/**
 * Translates result set into domain object.
 */
public class AccountAssembler {

    public Account assemble(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Account(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Currency.getInstance(resultSet.getString("currency")),
                resultSet.getDouble("balance")
            );
        }

        return null;
    }
}
