package de.ebuchner.vocab.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringReader {

    private StringReader() {

    }

    public static String readString(InputStream inputStream, String encoding) throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            return new String(out.toByteArray(), encoding);
        } finally {
            inputStream.close();
        }
    }
}
