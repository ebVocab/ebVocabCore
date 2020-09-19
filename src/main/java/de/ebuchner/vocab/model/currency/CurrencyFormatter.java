package de.ebuchner.vocab.model.currency;

import java.util.Currency;

public interface CurrencyFormatter {
    String format(Currency currency, double currencyValue);
}
