package de.ebuchner.vocab.model.currency;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

public class RupeeCurrencyFormatter implements CurrencyFormatter {

    private final static NumberFormat LONG_FMT_1_OR_MORE = new DecimalFormat("#0");
    private final static NumberFormat LONG_FMT_2 = new DecimalFormat("00");
    private final static NumberFormat LONG_FMT_3 = new DecimalFormat("000");

    private final static String INR = "INR";

    public String format(Currency currency, double currencyValue) {
        if (currency == null || Double.isNaN(currencyValue))
            return null;

        if (!currency.getCurrencyCode().equals(INR))
            return null;

        long intVal = Double.valueOf(Math.floor(currencyValue)).longValue();
        long paiseVal = Double.valueOf(Math.round((currencyValue - Math.floor(currencyValue)) * 100.0)).longValue();
        if (paiseVal > 99) {
            paiseVal = paiseVal - 100;
            intVal = intVal + 1;
        }
        long thousandVal = (intVal % 1000L);
        long hundredThousandVal = (intVal % (100L * 1000L)) / 1000L;
        long lakhVal = (intVal % (10L * 1000L * 1000L)) / (100 * 1000L); // hundred thousand
        long croreVal = intVal / (10L * 1000L * 1000L);  // ten million

        StringBuilder valueBuf = new StringBuilder();
        valueBuf.append(LONG_FMT_1_OR_MORE.format(croreVal));
        valueBuf.append(',');
        valueBuf.append(LONG_FMT_2.format(lakhVal));
        valueBuf.append(',');
        valueBuf.append(LONG_FMT_2.format(hundredThousandVal));
        valueBuf.append(',');
        valueBuf.append(LONG_FMT_3.format(thousandVal));
        valueBuf.append('.');
        valueBuf.append(LONG_FMT_2.format(paiseVal));
        valueBuf.append(" (");

        StringBuilder text = new StringBuilder();
        if (croreVal == 1)
            text.append("1 crore").append(' ');
        else if (croreVal > 1)
            text.append(croreVal).append(" crores").append(' ');

        if (lakhVal == 1)
            text.append("1 lakh").append(' ');
        else if (lakhVal > 1)
            text.append(lakhVal).append(" lakhs").append(' ');

        if (hundredThousandVal > 0)
            text.append(LONG_FMT_1_OR_MORE.format(hundredThousandVal)).append(LONG_FMT_3.format(thousandVal)).append(" rupee");
        else if (thousandVal > 0)
            text.append(LONG_FMT_1_OR_MORE.format(thousandVal)).append(" rupee");

        if (paiseVal > 0) {
            if (intVal > 0)
                text.append(" and ");
            if (paiseVal == 1)
                text.append("1 paisa");
            else
                text.append(paiseVal).append(" paise");
        }

        if (intVal == 0 && paiseVal == 0)
            text.append("0 rupee");

        valueBuf.append(text.toString().trim()).append(')');
        return valueBuf.toString().trim();
    }
}