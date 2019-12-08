package io.github.yashchenkon.banking.api.rest.transaction.adapter;

import io.github.yashchenkon.banking.api.rest.common.body.EntityCreatedResponseV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.ProcessTransactionRequestV1_0;
import io.github.yashchenkon.banking.domain.service.transaction.TransactionService;
import io.github.yashchenkon.banking.domain.service.transaction.dto.ProcessTransactionDto;

import javax.inject.Inject;

public class TransactionsRestApiAdapterV1_0 {

    private final TransactionService transactionService;

    @Inject
    public TransactionsRestApiAdapterV1_0(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public EntityCreatedResponseV1_0 process(ProcessTransactionRequestV1_0 request) {
        String transactionId = transactionService.process(
            new ProcessTransactionDto(request.getSourceAccountId(), request.getTargetAccountId(), request.getAmount())
        );

        return new EntityCreatedResponseV1_0(transactionId);
    }
}
