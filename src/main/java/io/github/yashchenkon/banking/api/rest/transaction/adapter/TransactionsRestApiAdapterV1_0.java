package io.github.yashchenkon.banking.api.rest.transaction.adapter;

import io.github.yashchenkon.banking.api.common.body.EntityCreatedResponseV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.DepositMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.TransactionResponseBodyV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.TransferMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.WithdrawMoneyRequestV1_0;
import io.github.yashchenkon.banking.domain.model.transaction.Transaction;
import io.github.yashchenkon.banking.domain.service.transaction.TransactionService;
import io.github.yashchenkon.banking.domain.service.transaction.dto.DepositMoneyDto;
import io.github.yashchenkon.banking.domain.service.transaction.dto.TransferMoneyDto;
import io.github.yashchenkon.banking.domain.service.transaction.dto.WithdrawMoneyDto;

import javax.inject.Inject;

/**
 * Adapter from REST API to domain services.
 */
public class TransactionsRestApiAdapterV1_0 {

    private final TransactionService transactionService;

    @Inject
    public TransactionsRestApiAdapterV1_0(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public EntityCreatedResponseV1_0 transfer(TransferMoneyRequestV1_0 request) {
        String transactionId = transactionService.transfer(
            new TransferMoneyDto(request.getSourceAccountId(), request.getTargetAccountId(), request.getAmount())
        );

        return new EntityCreatedResponseV1_0(transactionId);
    }

    public EntityCreatedResponseV1_0 deposit(DepositMoneyRequestV1_0 request) {
        String transactionId = transactionService.deposit(
            new DepositMoneyDto(request.getTargetAccountId(), request.getAmount())
        );

        return new EntityCreatedResponseV1_0(transactionId);
    }

    public EntityCreatedResponseV1_0 withdraw(WithdrawMoneyRequestV1_0 request) {
        String transactionId = transactionService.withdraw(
            new WithdrawMoneyDto(request.getTargetAccountId(), request.getAmount())
        );

        return new EntityCreatedResponseV1_0(transactionId);
    }

    public TransactionResponseBodyV1_0 transactionOfId(String transactionId) {
        Transaction transaction = transactionService.transactionOf(transactionId);

        return new TransactionResponseBodyV1_0(
            transaction.id(), transaction.type().name(), transaction.sourceAccountId(), transaction.targetAccountId(), transaction.amount(), transaction.completedAt()
        );
    }
}
