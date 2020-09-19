package de.ebuchner.vocab.model.currency;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringValue;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;

import java.util.Currency;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyModel extends AbstractModel {

    private static final Logger LOGGER = Logger.getLogger(CurrencyModel.class.getName());
    private static final String CURRENCY_VALUE_FROM = "currency_value_from";
    private static final String CURRENCY_VALUE_TO = "currency_value_to";
    private Currency currencyFrom;
    private Currency currencyTo;
    private Double exchangeRate = Double.NaN;
    private Double conversionResult = Double.NaN;
    private Double conversionInput = Double.NaN;

    public static CurrencyModel getOrCreateCurrencyModel() {
        return (CurrencyModel) VocabModel.getInstance().getOrCreateModel(CurrencyModel.class);
    }

    @Override
    protected void fireModelChanged() {

    }

    @Override
    public void reSynchronize() {

    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        currencyFrom = Currency.getInstance(Locale.getDefault());

        String currencyName = Config.instance().currencyName();
        if (currencyName == null)
            currencyTo = currencyFrom;
        else
            currencyTo = Currency.getInstance(currencyName);

        StringValue valueFrom = (StringValue) preferenceValues.getName(CurrencyModel.class, CURRENCY_VALUE_FROM);
        if (valueFrom != null)
            try {
                currencyFrom = Currency.getInstance(valueFrom.asString());
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.INFO, "Error loading source currency: {0}", valueFrom.asString());
            }

        StringValue valueTo = (StringValue) preferenceValues.getName(CurrencyModel.class, CURRENCY_VALUE_TO);
        if (valueTo != null)
            try {
                currencyTo = Currency.getInstance(valueTo.asString());
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.INFO, "Error loading target currency: " + valueTo.asString());
            }
    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {
        if (currencyFrom != null) {
            StringValue valueFrom = new StringValue(currencyFrom.getCurrencyCode());
            preferenceValues.putName(CurrencyModel.class, CURRENCY_VALUE_FROM, valueFrom);
        }
        if (currencyTo != null) {
            StringValue valueTo = new StringValue(currencyTo.getCurrencyCode());
            preferenceValues.putName(CurrencyModel.class, CURRENCY_VALUE_TO, valueTo);
        }
    }

    public boolean hasExchangeRate() {
        return !exchangeRate.isNaN();
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public void clearExchangeRate() {
        exchangeRate = Double.NaN;
    }

    public Double getConversionResult() {
        return conversionResult;
    }

    public void setConversionResult(Double conversionResult) {
        this.conversionResult = conversionResult;
    }

    public void clearConversionResult() {
        this.conversionResult = Double.NaN;
    }

    public boolean hasConversionResult() {
        return !conversionResult.isNaN();
    }

    public Double getConversionInput() {
        return conversionInput;
    }

    public void setConversionInput(Double conversionInput) {
        this.conversionInput = conversionInput;
    }

    public boolean hasConversionInput() {
        return !conversionInput.isNaN();
    }

    public void clearConversionInput() {
        conversionInput = Double.NaN;
    }
}
