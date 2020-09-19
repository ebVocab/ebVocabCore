package de.ebuchner.vocab.nui.common;

import de.ebuchner.toolbox.i18n.I18NContext;

import java.util.Locale;

public abstract class I18NLocator {
    private I18NLocator() {

    }

    public static I18NContext locate() {
        //return new I18NContext(Locale.getDefault(), I18NLocator.class.getPackage().getName() + ".nui");
        return new I18NContext(Locale.ENGLISH, I18NLocator.class.getPackage().getName() + ".nui");
    }
}
