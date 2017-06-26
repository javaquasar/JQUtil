package com.javaquasar.util.crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author Java Quasar
 */
public class CryptUtils {

    public static String getMD5(String txt) throws NoSuchAlgorithmException {
        return cryptMessage(MessageDigest.getInstance("MD5"), txt).toUpperCase();
    }

    public static String getSHA1(String txt) throws NoSuchAlgorithmException {
        return cryptMessage(MessageDigest.getInstance("SHA1"), txt).toLowerCase();
    }

    public static String getSHA256(String txt) throws NoSuchAlgorithmException {
        return cryptMessage(MessageDigest.getInstance("SHA-256"), txt).toLowerCase();
    }

    private static String cryptMessage(MessageDigest md, String txt) {
        md.update(txt.getBytes());

        byte byteData[] = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }
}
