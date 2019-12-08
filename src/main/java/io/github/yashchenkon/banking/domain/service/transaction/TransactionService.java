package io.github.yashchenkon.banking.domain.service.transaction;

import io.github.yashchenkon.banking.domain.exception.AccountHasLowBalanceException;
import io.github.yashchenkon.banking.domain.exception.AccountNotFoundException;
import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.repository.account.AccountRepository;
import io.github.yashchenkon.banking.domain.repository.transaction.TransactionRepository;
import io.github.yashchenkon.banking.domain.service.transaction.dto.ProcessTransactionDto;

import java.time.Instant;
import java.util.UUID;

import javax.inject.Inject;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Inject
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public String process(ProcessTransactionDto processTransaction) {
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), processTransaction.sourceAccountId(),
            processTransaction.targetAccountId(), processTransaction.amount(), Instant.now());

        boolean sourceAccountExists = accountRepository.exists(processTransaction.sourceAccountId());
        if (!sourceAccountExists) {
            throw new AccountNotFoundException();
        }

        boolean withdrawSucceeded = accountRepository.withdraw(processTransaction.sourceAccountId(), transaction.amount());
        if (!withdrawSucceeded) {
            throw new AccountHasLowBalanceException();
        }

        boolean depositSucceeded = accountRepository.deposit(processTransaction.targetAccountId(), transaction.amount());
        if (!depositSucceeded) {
            throw new AccountNotFoundException();
        }

        transactionRepository.save(transaction);

        return transaction.id();
    }
}
