package de.ebuchner.vocab.model.translate;

import de.ebuchner.vocab.config.Config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TranslateURL {

    private static final String TRANSLATE_URL_PATTERN = "http://translate.google.com/#%s/%s/%s";

    private TranslateURL() {

    }

    public static String url(String vocab) throws UnsupportedEncodingException {
        return String.format(
                TRANSLATE_URL_PATTERN,
                Config.instance().getLocale(),
                Config.instance().getSystemLocale(),
                URLEncoder.encode(vocab, "UTF-8")
        );
    }
}
