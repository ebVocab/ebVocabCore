package de.ebuchner.vocab.model.currency;

import java.util.Comparator;
import java.util.Currency;

class CurrencyComparator implements Comparator<Currency> {

    public int compare(Currency c1, Currency c2) {
        return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
    }

}