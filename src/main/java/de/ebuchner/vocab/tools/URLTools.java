package de.ebuchner.vocab.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLTools {

    private URLTools() {

    }

    public static String asText(URL url) {
        return asText(url, "utf-8");
    }

    public static String asText(URL url, String encoding) {
        StringBuilder builder = new StringBuilder();
        try {
            char buffer[] = new char[4096];
            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream(), encoding));

            int readCount;
            while ((readCount = r.read(buffer)) > 0) {
                builder.append(buffer, 0, readCount);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder.toString();
    }
}
