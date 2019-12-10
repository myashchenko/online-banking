package io.github.yashchenkon.banking.domain.service.exchange.rate;

import java.util.Currency;

public interface CurrencyExchangeRateProvider {
    Double rateFor(Currency source, Currency target);
}
