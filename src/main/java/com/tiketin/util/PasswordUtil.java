package com.tiketin.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    private PasswordUtil(){

    }

    public static String hash(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hasil = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hasil) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritma SHA-256 tidak tersedia", e);
        }
    }
    public static boolean cocok(String passwordInput, String hashTersimpan){
        return hash(passwordInput).equals(hashTersimpan);
    }
}
