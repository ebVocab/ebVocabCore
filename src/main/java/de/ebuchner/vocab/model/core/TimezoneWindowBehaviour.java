package de.ebuchner.vocab.model.core;

import java.util.TimeZone;

public interface TimezoneWindowBehaviour {
    void setTimezoneUser(TimeZone timezoneUser);

    void setTimezoneForeign(TimeZone timezoneForeign);

    void setInputEnabled(boolean b);

    void setInput(String input);

    TimeZone getTimezoneUser();

    TimeZone getTimezoneForeign();

    void setResult(String result);

    String getInput();
}
