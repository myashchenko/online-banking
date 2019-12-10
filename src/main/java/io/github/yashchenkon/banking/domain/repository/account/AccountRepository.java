package io.github.yashchenkon.banking.domain.repository.account;

import io.github.yashchenkon.banking.domain.model.account.Account;

/**
 * Account repository is responsible for storing account-related information
 */
public interface AccountRepository {

    /**
     * Loads an object from the storage.
     *
     * @param id - id of the object ot load
     * @return account
     */
    Account accountOfId(String id);

    /**
     * Persist an object into storage.
     *
     * @param account - object to persist
     */
    void save(Account account);

    /**
     * Checks whether an object exists in the storage.
     *
     * @param id - id of the object
     * @return true if object exists, otherwise - false
     */
    boolean exists(String id);

    /**
     * Withdraws money from the account.
     *
     * @param id - id of the account to withdraw money from
     * @param amount - amount to withdraw
     * @return true if withdraw has been successful (e.g account exists and has enough money), otherwise - false
     */
    boolean withdraw(String id, Double amount);

    /**
     * Deposits money to the account.
     *
     * @param id - id of the account to deposit money to
     * @param amount - amount to deposit
     * @return true if deposit has been successful (e.g account exists), otherwise - false
     */
    boolean deposit(String id, Double amount);
}
