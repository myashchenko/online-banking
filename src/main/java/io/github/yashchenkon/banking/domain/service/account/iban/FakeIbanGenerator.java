package io.github.yashchenkon.banking.domain.service.account.iban;

import java.util.UUID;

/**
 * Fake IBAN generator returning random UUID.
 */
public class FakeIbanGenerator implements IbanGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
