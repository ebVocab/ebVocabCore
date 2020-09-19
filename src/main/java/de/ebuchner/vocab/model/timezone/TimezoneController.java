package de.ebuchner.vocab.model.timezone;

import de.ebuchner.vocab.model.VocabBaseController;
import de.ebuchner.vocab.model.core.TimezoneWindowBehaviour;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

public class TimezoneController extends VocabBaseController {

    private TimezoneWindowBehaviour timezoneWindow;
    private TimezoneModel timezoneModel;

    public TimezoneController(TimezoneWindowBehaviour timezoneWindow) {
        this.timezoneWindow = timezoneWindow;
        this.timezoneModel = TimezoneModel.getOrCreateTimezoneModel();
    }

    public void onWindowCreated() {
        timezoneWindow.setTimezoneUser(timezoneModel.getTimezoneUser());
        timezoneWindow.setTimezoneForeign(timezoneModel.getTimezoneForeign());
        timezoneWindow.setInputEnabled(true);
        timezoneWindow.setResult(null);
        onResetInput();
    }

    public void onTimezoneChanged() {
        timezoneWindow.setInputEnabled(false);
        timezoneWindow.setResult(null);

        if (timezoneWindow.getTimezoneUser() != null && timezoneWindow.getTimezoneForeign() != null) {
            timezoneWindow.setInputEnabled(true);
        }
    }

    public void onResetInput() {
        timezoneWindow.setInput(timezoneModel.formatUserDate(new Date()));
    }

    public void onConvert() throws ParseException {
        timezoneWindow.setResult(null);
        TimeZone timeZoneUser = timezoneWindow.getTimezoneUser();
        TimeZone timeZoneForeign = timezoneWindow.getTimezoneForeign();
        Date input = timezoneModel.parseUserDate(timezoneWindow.getInput());

        if (timeZoneForeign == null || timeZoneUser == null || input == null)
            return;

        timezoneWindow.setResult(
                timezoneModel.convertDateBetweenTimezones(
                        input, timeZoneUser, timeZoneForeign
                )
        );
    }
}
