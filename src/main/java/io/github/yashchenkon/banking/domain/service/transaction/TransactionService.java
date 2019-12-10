package io.github.yashchenkon.banking.domain.service.transaction;

import io.github.yashchenkon.banking.domain.exception.AccountHasLowBalanceException;
import io.github.yashchenkon.banking.domain.exception.AccountNotFoundException;
import io.github.yashchenkon.banking.domain.exception.TransactionNotFoundException;
import io.github.yashchenkon.banking.domain.factory.transaction.TransactionFactory;
import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.model.transaction.TransactionType;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.transaction.TransactionRepository;
import io.github.yashchenkon.banking.domain.service.exchange.CurrencyExchangeService;
import io.github.yashchenkon.banking.domain.service.transaction.dto.DepositMoneyDto;
import io.github.yashchenkon.banking.domain.service.transaction.dto.TransferMoneyDto;
import io.github.yashchenkon.banking.domain.service.transaction.dto.WithdrawMoneyDto;
import io.github.yashchenkon.banking.infra.database.TransactionalExecutor;

import java.util.Currency;

import javax.inject.Inject;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionFactory transactionFactory;
    private final TransactionalExecutor transactionalExecutor;
    private final CurrencyExchangeService currencyExchangeService;

    @Inject
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                              TransactionFactory transactionFactory, TransactionalExecutor transactionalExecutor,
                              CurrencyExchangeService currencyExchangeService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionFactory = transactionFactory;
        this.transactionalExecutor = transactionalExecutor;
        this.currencyExchangeService = currencyExchangeService;
    }

    /**
     * Transfers money from one account to another.
     *
     * @param transferMoney - details about transaction
     * @return transaction id
     */
    public String transfer(TransferMoneyDto transferMoney) {
        Transaction transaction = transactionFactory.create(TransactionType.TRANSFER,
            transferMoney.sourceAccountId(), transferMoney.targetAccountId(), transferMoney.amount());

        transactionalExecutor.execute(() -> {
            // avoid deadlock
            boolean withdrawFirst = transferMoney.sourceAccountId().compareTo(transferMoney.targetAccountId()) > 0;

            Currency sourceCurrency = accountRepository.currencyOfAccount(transferMoney.sourceAccountId());
            Currency targetCurrency = accountRepository.currencyOfAccount(transferMoney.targetAccountId());
            if (sourceCurrency == null || targetCurrency == null) {
                throw new AccountNotFoundException();
            }

            Double amountToWithdraw = transaction.amount();
            Double amountToDeposit = currencyExchangeService.exchange(sourceCurrency, targetCurrency, amountToWithdraw);

            if (withdrawFirst) {
                withdraw(transferMoney.sourceAccountId(), amountToWithdraw);
                deposit(transferMoney.targetAccountId(), amountToDeposit);
            } else {
                deposit(transferMoney.targetAccountId(), amountToDeposit);
                withdraw(transferMoney.sourceAccountId(), amountToWithdraw);
            }

            transactionRepository.save(transaction);
        });

        return transaction.id();
    }

    /**
     * Puts money onto account.
     *
     * @param depositMoney - details about transaction
     * @return transaction id
     */
    public String deposit(DepositMoneyDto depositMoney) {
        Transaction transaction = transactionFactory.create(TransactionType.DEPOSIT, depositMoney.targetAccountId(), depositMoney.amount());

        transactionalExecutor.execute(() -> {
            boolean accountExists = accountRepository.exists(depositMoney.targetAccountId());
            if (!accountExists) {
                throw new AccountNotFoundException();
            }

            deposit(depositMoney.targetAccountId(), depositMoney.amount());
            transactionRepository.save(transaction);
        });

        return transaction.id();
    }

    private void deposit(String accountId, Double amount) {
        boolean depositSucceeded = accountRepository.deposit(accountId, amount);
        if (!depositSucceeded) {
            throw new AccountNotFoundException();
        }
    }

    /**
     * Withdraws money from the account.
     *
     * @param withdrawMoney - details about transaction
     * @return transaction id
     */
    public String withdraw(WithdrawMoneyDto withdrawMoney) {
        Transaction transaction = transactionFactory.create(TransactionType.WITHDRAW, withdrawMoney.targetAccountId(), withdrawMoney.amount());

        transactionalExecutor.execute(() -> {
            boolean accountExists = accountRepository.exists(withdrawMoney.targetAccountId());
            if (!accountExists) {
                throw new AccountNotFoundException();
            }

            withdraw(withdrawMoney.targetAccountId(), withdrawMoney.amount());
            transactionRepository.save(transaction);
        });

        return transaction.id();
    }

    private void withdraw(String accountId, Double amount) {
        boolean withdrawSucceeded = accountRepository.withdraw(accountId, amount);
        if (!withdrawSucceeded) {
            throw new AccountHasLowBalanceException();
        }
    }

    /**
     * Find transaction by id.
     *
     * @param id - id of the transaction
     * @return transaction
     */
    public Transaction transactionOf(String id) {
        Transaction transaction = transactionalExecutor.executeReadOnly(() -> transactionRepository.transactionOfId(id));

        if (transaction == null) {
            throw new TransactionNotFoundException();
        }

        return transaction;
    }
}
