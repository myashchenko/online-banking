package io.github.yashchenkon.banking.api.rest.transaction.validator;

import io.github.yashchenkon.banking.api.common.FieldNames;
import io.github.yashchenkon.banking.api.common.exception.ValidationFailedException;
import io.github.yashchenkon.banking.api.rest.transaction.body.ProcessTransactionRequestV1_0;

public class TransactionsRestApiValidatorV1_0 {

    public void validate(ProcessTransactionRequestV1_0 request) {
        if (request.getSourceAccountId() == null || request.getSourceAccountId().isBlank()) {
            throw new ValidationFailedException(FieldNames.SOURCE_ACCOUNT_ID, ValidationFailedException.ValidationStatus.BLANK);
        }
        if (request.getTargetAccountId() == null || request.getTargetAccountId().isBlank()) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.BLANK);
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }
        if (request.getSourceAccountId().equals(request.getTargetAccountId())) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }
    }
}
