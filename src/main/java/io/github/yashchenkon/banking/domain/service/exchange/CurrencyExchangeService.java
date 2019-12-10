package io.github.yashchenkon.banking.domain.service.exchange;

import io.github.yashchenkon.banking.domain.service.exchange.rate.CurrencyExchangeRateProvider;

import java.util.Currency;

import javax.inject.Inject;

public class CurrencyExchangeService {

    private final CurrencyExchangeRateProvider exchangeRateProvider;

    @Inject
    public CurrencyExchangeService(CurrencyExchangeRateProvider exchangeRateProvider) {
        this.exchangeRateProvider = exchangeRateProvider;
    }

    public Double exchange(Currency source, Currency target, Double value) {
        return exchangeRateProvider.rateFor(source, target) * value;
    }
}
