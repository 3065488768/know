package com.example.know.util;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author bookWorm
 */

public class RandomUtil {
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random random = new Random();

    public static String getRandomValue() {
        char[] nonceChars = new char[4];
        for (int i = 0; i < nonceChars.length; i++) {
            nonceChars[i] = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}
