package de.ebuchner.toolbox.i18n;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class I18NContext {

    private final Locale locale;
    private final ResourceBundle res;
    private ResourceBundle sys;

    /**
     * Creates a context for the specified locale and looks for ui.properties
     * and optionally sys.properties in the package that the given class belongs to
     */
    public I18NContext(Locale locale, Class baseClass) {
        this(locale,
                baseClass.getPackage().getName() + ".ui",
                baseClass.getPackage().getName() + ".sys");
    }

    public I18NContext(Locale locale, String uiBundleName) {
        this(locale, uiBundleName, null);
    }

    public I18NContext(Locale locale, String uiBundleName, String sysBundleName) {
        this.locale = locale;
        // otherwise JUnit ant tests fail with "bundle not found"!?
        ClassLoader cl = I18NContext.class.getClassLoader();

        this.res = ResourceBundle.getBundle(uiBundleName, locale, cl);
        if (sysBundleName != null)
            try {
                this.sys = ResourceBundle.getBundle(sysBundleName, locale, cl);
            } catch (MissingResourceException mre) {
                // optional
            }
    }

    /**
     * Creates a context with default locale and looks for ui.properties
     * and optionally sys.properties in the package that the given class belongs to
     */
    public static I18NContext getDefault(Class baseClass) {
        return new I18NContext(Locale.getDefault(), baseClass);
    }

    public String formatDate(String patternKey, Date d) {
        return new SimpleDateFormat(getString(patternKey), locale).format(d);
    }

    public String formatNumber(String patternKey, Number n) {
        return new DecimalFormat(getString(patternKey), new DecimalFormatSymbols(locale)).format(n);
    }

    public String getString(String name) {
        try {
            return res.getString(name);
        } catch (MissingResourceException mre) {
            if (sys == null)
                throw mre;
            return sys.getString(name);
        }
    }

    public String getString(String name, List args) {
        return MessageFormat.format(getString(name), args.toArray());
    }

    public String getOptionalString(String name, String value) {
        try {
            return getString(name);
        } catch (MissingResourceException mre) {
            return value;
        }
    }

    public String getOptionalString(String name, String value, List args) {
        return MessageFormat.format(getOptionalString(name, value), args.toArray());
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getResources() {
        return new CombinedResourceBundle();
    }

    private class CombinedResourceBundle extends ResourceBundle {

        @Override
        protected Object handleGetObject(String key) {
            return I18NContext.this.getString(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            Vector<String> keys = new Vector<String>();
            for (Enumeration en = res.getKeys(); en.hasMoreElements(); ) {
                keys.add(String.valueOf(en.nextElement()));
            }
            for (Enumeration en = res.getKeys(); en.hasMoreElements(); ) {
                String key = String.valueOf(en.nextElement());
                if (!keys.contains(key))
                    keys.add(key);
            }
            return keys.elements();
        }
    }
}
