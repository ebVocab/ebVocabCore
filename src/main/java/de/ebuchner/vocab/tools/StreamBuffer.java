package de.ebuchner.vocab.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * reads a stream into separate buffer and leaves leaves the original stream open
 * (for SAXParser who closes the stream otherwise)
 */

public class StreamBuffer {
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public StreamBuffer(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) >= 0)
                outputStream.write(buffer, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public InputStream duplicateInputStream() {
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
