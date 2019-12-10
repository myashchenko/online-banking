package io.github.yashchenkon.banking.api.rest.account.validator;

import io.github.yashchenkon.banking.api.common.FieldNames;
import io.github.yashchenkon.banking.api.common.exception.ValidationFailedException;
import io.github.yashchenkon.banking.api.rest.account.body.CreateAccountRequestV1_0;

import java.util.Currency;
import java.util.List;

/**
 * Validates REST API requests.
 */
public class AccountRestApiValidatorV1_0 {

    private static final List<String> SUPPORTED_CURRENCIES = List.of("USD", "EUR");

    public void validate(CreateAccountRequestV1_0 request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationFailedException(FieldNames.NAME, ValidationFailedException.ValidationStatus.BLANK);
        }

        if (request.getCurrency() == null || request.getCurrency().isBlank()) {
            throw new ValidationFailedException(FieldNames.CURRENCY, ValidationFailedException.ValidationStatus.BLANK);
        }

        try {
            Currency.getInstance(request.getCurrency());
        } catch (Exception e) {
            throw new ValidationFailedException(FieldNames.CURRENCY, ValidationFailedException.ValidationStatus.INVALID_VALUE);
        }

        if (!SUPPORTED_CURRENCIES.contains(request.getCurrency())) {
            throw new ValidationFailedException(FieldNames.CURRENCY, ValidationFailedException.ValidationStatus.UNSUPPORTED_VALUE);
        }
    }
}
