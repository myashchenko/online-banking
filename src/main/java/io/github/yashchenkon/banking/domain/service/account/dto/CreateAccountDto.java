package io.github.yashchenkon.banking.domain.service.account.dto;

public class CreateAccountDto {
    private final String name;
    private final String currency;

    public CreateAccountDto(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public String name() {
        return name;
    }

    public String currency() {
        return currency;
    }
}
