package com.tiketin.service;

import com.tiketin.model.Pengguna;
import com.tiketin.util.PasswordUtil;

public class AuthService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public Pengguna login(String username, String password) {
        String namaAkun = username == null ? "" : username.trim();
        if (namaAkun.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username dan password wajib diisi.");
        }
        Pengguna pengguna = data.cariUsername(namaAkun);
        if (pengguna == null || !PasswordUtil.cocok(password, pengguna.getPassword())) {
            throw new IllegalArgumentException("Username atau password salah.");
        }
        return pengguna;
    }
}
