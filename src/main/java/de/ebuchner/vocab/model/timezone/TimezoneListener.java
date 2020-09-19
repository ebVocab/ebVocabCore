package de.ebuchner.vocab.model.timezone;

import de.ebuchner.vocab.model.core.ModelListener;

public interface TimezoneListener extends ModelListener {
    void timezoneModelChanged();
}
