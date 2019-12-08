package io.github.yashchenkon.banking.domain.repository.transaction;

import io.github.yashchenkon.banking.domain.model.transaction.Transaction;

public interface TransactionRepository {

    void save(Transaction transaction);

    Transaction transactionOfId(String id);
}
