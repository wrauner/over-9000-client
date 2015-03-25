package com.mkoi.over9000.secure;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Wojciech Rauner
 */
public class PasswordUtil {
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String getHashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        digest.update(password.getBytes());
        byte[] hashedBytes = digest.digest();
        return Base64.encodeToString(hashedBytes, Base64.DEFAULT);
    }
}
