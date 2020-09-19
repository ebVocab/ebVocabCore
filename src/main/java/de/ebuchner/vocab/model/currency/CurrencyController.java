package de.ebuchner.vocab.model.currency;

import de.ebuchner.vocab.config.Config;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.TreeSet;

public class CurrencyController {

    private CurrencyWindowBehaviour currencyWindow;
    private CurrencyModel currencyModel;
    private NumberFormat rateFormat = new DecimalFormat("#0.000");

    public CurrencyController(CurrencyWindowBehaviour currencyWindow) {
        this.currencyWindow = currencyWindow;
        this.currencyModel = CurrencyModel.getOrCreateCurrencyModel();
    }

    public Currency[] currencies() {
        TreeSet<Currency> sortedSet = new TreeSet<Currency>(
                new CurrencyComparator()
        );
        sortedSet.addAll(Currency.getAvailableCurrencies());

        return sortedSet.toArray(new Currency[0]);
    }

    public void onDataChanged(Currency currencyFrom, Currency currencyTo) {
        if (currencyModel.getCurrencyFrom() != currencyFrom ||
                currencyModel.getCurrencyTo() != currencyTo) {
            currencyModel.setCurrencyFrom(currencyFrom);
            currencyModel.setCurrencyTo(currencyTo);
            currencyModel.clearExchangeRate();
            currencyModel.clearConversionInput();
            currencyModel.clearConversionResult();
        }

        currencyWindow.resetControls();

        currencyWindow.setInputSymbolText(currencyFrom.getCurrencyCode());
        currencyWindow.setResultSymbolText(currencyTo.getCurrencyCode());

        if (currencyModel.hasExchangeRate()) {
            currencyWindow.setInputEnabled(true);
            currencyWindow.setExchangeRateText(
                    "1 " +
                            currencyFrom.getCurrencyCode() +
                            " = " +
                            rateFormat.format(currencyModel.getExchangeRate()) +
                            " " +
                            currencyTo.getCurrencyCode()
            );
        }

        if (currencyModel.hasConversionResult()) {
            currencyWindow.setConversionResult(currencyModel.getConversionResult());
        }

        if (currencyModel.hasConversionInput()) {
            currencyWindow.setConversionInput(currencyModel.getConversionInput());
        }

    }

    public Currency currencyFrom() {
        return currencyModel.getCurrencyFrom();
    }

    public Currency currencyTo() {
        return currencyModel.getCurrencyTo();
    }

    public void onWindowWasCreated() {
        onDataChanged(currencyFrom(), currencyTo());
    }

    @Deprecated
    public void onRefreshExchangeRate(Currency currencyFrom, Currency currencyTo) {
        onDataChanged(currencyFrom, currencyTo);

        double exchangeRate = YahooFinanceCurrency.obtainRate(currencyFrom.getCurrencyCode(), currencyTo.getCurrencyCode());
        currencyModel.setExchangeRate(exchangeRate);

        onDataChanged(currencyFrom, currencyTo);
    }

    @Deprecated
    public void onConvert(double amount) {
        currencyModel.setConversionInput(amount);
        currencyModel.setConversionResult(currencyModel.getExchangeRate() * amount);
        onDataChanged(
                currencyFrom(), currencyTo()
        );
    }

    public CurrencyFormatter getCurrencyFormatter() {
        String className = Config.instance().currencyFormatterClassName();
        if (className == null)
            return null;

        try {
            return (CurrencyFormatter) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
