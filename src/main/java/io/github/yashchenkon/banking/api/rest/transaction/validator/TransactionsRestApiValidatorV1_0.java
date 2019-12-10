package io.github.yashchenkon.banking.api.rest.transaction.validator;

import io.github.yashchenkon.banking.api.common.FieldNames;
import io.github.yashchenkon.banking.api.common.exception.ValidationFailedException;
import io.github.yashchenkon.banking.api.rest.transaction.body.DepositMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.TransferMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.WithdrawMoneyRequestV1_0;

/**
 * Validates REST API requests.
 */
public class TransactionsRestApiValidatorV1_0 {

    public void validate(TransferMoneyRequestV1_0 request) {
        if (request.getSourceAccountId() == null || request.getSourceAccountId().isBlank()) {
            throw new ValidationFailedException(FieldNames.SOURCE_ACCOUNT_ID, ValidationFailedException.ValidationStatus.BLANK);
        }
        if (request.getTargetAccountId() == null || request.getTargetAccountId().isBlank()) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.BLANK);
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new ValidationFailedException(FieldNames.AMOUNT, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }
        if (request.getSourceAccountId().equals(request.getTargetAccountId())) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }
    }

    public void validate(DepositMoneyRequestV1_0 request) {
        if (request.getTargetAccountId() == null || request.getTargetAccountId().isBlank()) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.BLANK);
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new ValidationFailedException(FieldNames.AMOUNT, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }
    }

    public void validate(WithdrawMoneyRequestV1_0 request) {
        if (request.getTargetAccountId() == null || request.getTargetAccountId().isBlank()) {
            throw new ValidationFailedException(FieldNames.TARGET_ACCOUNT_ID, ValidationFailedException.ValidationStatus.BLANK);
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new ValidationFailedException(FieldNames.AMOUNT, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }
    }
}
