package de.ebuchner.vocab.model.currency;

import de.ebuchner.vocab.tools.URLTools;

import java.net.MalformedURLException;
import java.net.URL;

public class YahooFinanceCurrency {

    private YahooFinanceCurrency() {

    }

    @Deprecated // the yahoo URL is no longer supported
    public static double obtainRate(String currencyFrom, String currencyTo) {
        currencyFrom = fixSymbol(currencyFrom);
        currencyTo = fixSymbol(currencyTo);

        URL yahooFinanceURL;
        try {
            yahooFinanceURL = new URL("http://quote.yahoo.com/d/quotes.csv?s=" + currencyFrom + currencyTo + "=X&f=l1&e=.csv");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String result = URLTools.asText(yahooFinanceURL);
        return Double.parseDouble(result);
    }

    private static String fixSymbol(String currencyCode) {
        if (currencyCode.equals("\u20ac"))
            return "EUR";
        else if (currencyCode.equals("$"))
            return "USD";
        return currencyCode;
    }
}