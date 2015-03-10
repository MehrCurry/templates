package de.gzockoll.prototype.templates.validation;

import com.google.common.io.ByteStreams;

import java.io.IOException;

public class PDFHelper {
    public static  byte[] getValidPDFContent() {
        try {
            return ByteStreams.toByteArray(ClassLoader.getSystemResourceAsStream("produkte.pdf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
