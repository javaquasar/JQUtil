package com.javaquasar.util.math;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 *
 * @author Java Quasar
 */
public class Random {

    private static final SecureRandom random = new SecureRandom();
    private static final char[] charsArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(nextSessionId());
            System.out.println(getRandomUUID());
        }
    }

    /**
     * This works by choosing 130 bits from a cryptographically secure random
     * bit generator, and encoding them in base-32. 128 bits is considered to be
     * cryptographically strong, but each digit in a base 32 number can encode 5
     * bits, so 128 is rounded up to the next multiple of 5. This encoding is
     * compact and efficient, with 5 random bits per character. Compare this to
     * a random UUID, which only has 3.4 bits per character in standard layout,
     * and only 122 random bits in total.
     *
     * If you allow session identifiers to be easily guessable (too short,
     * flawed random number generator, etc.), attackers can hijack other's
     * sessions. Note that SecureRandom objects are expensive to initialize, so
     * you'll want to keep one around and reuse it.
     *
     * Here is alternative code for cheap, insecure random alpha-numeric
     * strings. You can tweak the "symbols" if you want to use more characters.
     *
     * @return random string
     */
    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

//    public static String getRandomString(char[] chars) {
//        if (chars == null || chars.length <= 0) {
//            chars = charsArray;
//        }
//        StringBuilder sb = new StringBuilder();
//        Random random2 = new Random();
//        for (int i = 0; i < 20; i++) {
//            char c = chars[random2.nextInt(chars.length)];
//            sb.append(c);
//        }
//        return sb.toString();
//    }
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
