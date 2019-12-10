package io.github.yashchenkon.banking.domain.service.exchange.rate;

import com.google.common.base.Objects;

import io.github.yashchenkon.banking.common.CurrencyCodes;

import java.util.Currency;
import java.util.Map;
import java.util.Optional;

public class InMemoryExchangeRateProvider implements CurrencyExchangeRateProvider {

    private static final Map<CurrencyPair, Double> RATES = Map.of(
        new CurrencyPair(CurrencyCodes.USD, CurrencyCodes.EUR), 0.9
    );

    @Override
    public Double rateFor(Currency source, Currency target) {
        if (source.getCurrencyCode().equals(target.getCurrencyCode())) {
            return 1.0;
        }

        return Optional.ofNullable(RATES.get(new CurrencyPair(source.getCurrencyCode(), target.getCurrencyCode())))
            .orElseGet(() -> 1.0 / RATES.get(new CurrencyPair(target.getCurrencyCode(), source.getCurrencyCode())));
    }

    private static final class CurrencyPair {
        private final String source;
        private final String target;

        private CurrencyPair(String source, String target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CurrencyPair that = (CurrencyPair) o;
            return Objects.equal(source, that.source) &&
                Objects.equal(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(source, target);
        }
    }
}
