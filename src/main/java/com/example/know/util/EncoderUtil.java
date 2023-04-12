package com.example.know.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Base64;

/**
 * @author bookWorm
 */
public class EncoderUtil {

    private final static Base64.Encoder encoder = Base64.getEncoder();

    public static String encoder(FileInputStream inputStream) throws IOException {
        Base64.Encoder encoder = Base64.getEncoder();
        int available = inputStream.available();
        byte[] bytes = new byte[available];
        inputStream.read(bytes);
        inputStream.close();
        return encoder.encodeToString(bytes);
    }
}
