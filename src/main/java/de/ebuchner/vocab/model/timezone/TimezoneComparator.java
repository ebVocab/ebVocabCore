package de.ebuchner.vocab.model.timezone;

import java.util.Comparator;
import java.util.TimeZone;

class TimezoneComparator implements Comparator<TimeZone> {
    public int compare(TimeZone tz1, TimeZone tz2) {
        int diff = new Integer(tz1.getRawOffset()).compareTo(tz2.getRawOffset());
        if (diff != 0)
            return diff;
        diff = tz1.getDisplayName().compareTo(tz2.getDisplayName());
        if (diff != 0)
            return diff;
        return tz1.getID().compareTo(tz2.getID());
    }
}
