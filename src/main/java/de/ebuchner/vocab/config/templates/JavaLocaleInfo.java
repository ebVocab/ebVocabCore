package de.ebuchner.vocab.config.templates;

import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class JavaLocaleInfo {

    private JavaLocaleInfo() {

    }

    public static void main(String args[]) {

        System.out.println("\nLocales:\n");
        for (Locale locale : Locale.getAvailableLocales()) {
            System.out.println(" " + locale.getDisplayName());
        }

        System.out.println("\nTimezones:\n");
        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone tz = TimeZone.getTimeZone(id);
            System.out.println(" " + tz.getID() + " - " + tz.getDisplayName());
        }

        System.out.println("\nCurrencies:\n");
        for (Currency cy : Currency.getAvailableCurrencies()) {
            System.out.println(" " + cy.getCurrencyCode() + " - " + cy.getDisplayName());
        }
    }
}
