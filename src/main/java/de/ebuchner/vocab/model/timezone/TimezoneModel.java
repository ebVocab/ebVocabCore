package de.ebuchner.vocab.model.timezone;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.commands.SimpleCommand;
import de.ebuchner.vocab.model.core.AbstractModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeSet;

public class TimezoneModel extends AbstractModel<SimpleCommand, TimezoneListener> {

    private Collection<TimeZone> timezones;

    private TimeZone timezoneUser = TimeZone.getDefault();

    private SimpleDateFormat dateFormatFrom = new SimpleDateFormat("dd.MM.yy HH:mm");
    private SimpleDateFormat dateFormatTo = new SimpleDateFormat("dd.MM.yy HH:mm");

    public TimezoneModel() {
        timezones = new TreeSet<TimeZone>(
                new TimezoneComparator()
        );

        for (String id : TimeZone.getAvailableIDs()) {
            timezones.add(TimeZone.getTimeZone(id));
        }
    }

    public static TimezoneModel getOrCreateTimezoneModel() {
        return (TimezoneModel) VocabModel.getInstance().getOrCreateModel(TimezoneModel.class);
    }

    public TimeZone[] timezonesArray() {
        return timezones.toArray(new TimeZone[0]);
    }

    @Override
    protected void fireModelChanged() {
        for (TimezoneListener listener : listeners) {
            listener.timezoneModelChanged();
        }
    }

    @Override
    public void reSynchronize() {

    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {

    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {

    }

    public TimeZone getTimezoneForeign() {
        String timeZoneID = Config.instance().timezoneID();
        if (timeZoneID == null)
            return getTimezoneUser();
        return TimeZone.getTimeZone(timeZoneID);
    }

    public TimeZone getTimezoneUser() {
        return timezoneUser;
    }

    public String convertDateBetweenTimezones(Date input, TimeZone timeZoneUser, TimeZone timeZoneForeign) {
        dateFormatFrom.setTimeZone(timeZoneUser);
        dateFormatTo.setTimeZone(timeZoneForeign);

        return dateFormatTo.format(input);

    }

    public String formatUserDate(Date date) {
        dateFormatFrom.setTimeZone(timezoneUser);
        return dateFormatFrom.format(date);
    }

    public Date parseUserDate(String input) throws ParseException {
        dateFormatFrom.setTimeZone(timezoneUser);
        return dateFormatFrom.parse(input);
    }
}
