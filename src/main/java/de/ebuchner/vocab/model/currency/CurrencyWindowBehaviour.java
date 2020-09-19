package de.ebuchner.vocab.model.currency;

public interface CurrencyWindowBehaviour {
    void resetControls();

    void setInputSymbolText(String symbol);

    void setResultSymbolText(String symbol);

    void setInputEnabled(boolean enabled);

    void setExchangeRateText(String text);

    void setConversionInput(double input);

    void setConversionResult(double result);
}
