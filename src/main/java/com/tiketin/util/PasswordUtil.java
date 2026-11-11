package com.tiketin.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public final class PasswordUtil {
    private static final SecureRandom ACAK = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hash(String plain) {
        byte[] salt = new byte[16];
        ACAK.nextBytes(salt);
        return HexFormat.of().formatHex(salt) + ":" + hash(salt, plain);
    }

    public static boolean cocok(String plain, String tersimpan) {
        if (plain == null || tersimpan == null || !tersimpan.contains(":")) {
            return false;
        }
        String[] bagian = tersimpan.split(":", 2);
        byte[] salt = HexFormat.of().parseHex(bagian[0]);
        return hash(salt, plain).equalsIgnoreCase(bagian[1]);
    }

    private static String hash(byte[] salt, String plain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hasil = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hasil);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 tidak tersedia.", ex);
        }
    }
}
