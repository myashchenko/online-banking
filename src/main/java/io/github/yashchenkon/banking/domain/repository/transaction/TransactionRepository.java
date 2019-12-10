package io.github.yashchenkon.banking.domain.repository.transaction;

import io.github.yashchenkon.banking.domain.model.transaction.Transaction;

/**
 * Transaction repository is responsible for storing the history of transactions
 */
public interface TransactionRepository {

    /**
     * Persist an object into storage.
     *
     * @param transaction - object to persist
     */
    void save(Transaction transaction);

    /**
     * Loads an object from the storage.
     *
     * @param id - id of the object ot load
     * @return transaction
     */
    Transaction transactionOfId(String id);
}
