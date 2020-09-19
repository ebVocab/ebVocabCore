package de.ebuchner.vocab.model.cloud;

public abstract class CloudList {
    protected String extract(String stringValue) {
        if (stringValue.startsWith("'"))
            stringValue = stringValue.substring(1);
        if (stringValue.endsWith("'"))
            stringValue = stringValue.substring(0, stringValue.length() - 1);
        return stringValue;
    }

}
