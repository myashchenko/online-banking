package io.github.yashchenkon.banking.domain.service.account.iban;

import java.util.UUID;

public class IbanGenerator {

    public String generate() {
        return UUID.randomUUID().toString().substring(0, 32);
    }
}
