package com.tiketin.service;

import com.tiketin.model.Admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

public class ProfilService {
    private static final Path BERKAS = Path.of("data", "profil-admin.properties");

    private final PenyimpananSementara data = PenyimpananSementara.get();

    public static void terapkanTersimpan(Admin admin) {
        if (!Files.isRegularFile(BERKAS)) {
            return;
        }
        Properties isi = new Properties();
        try (InputStream masuk = Files.newInputStream(BERKAS)) {
            isi.load(masuk);
        } catch (IOException ex) {
            return;
        }
        String nama = isi.getProperty("nama", "").trim();
        String username = isi.getProperty("username", "").trim();
        String foto = isi.getProperty("foto", "").trim();
        if (!nama.isBlank()) {
            admin.setNama(nama);
        }
        if (!username.isBlank()) {
            admin.setUsername(username);
        }
        if (!foto.isBlank() && Files.isRegularFile(Path.of(foto))) {
            admin.setFoto(foto);
        }
    }

    public void simpan(Admin admin, String nama, String username, Path fotoBaru, boolean hapusFoto) {
        String namaBersih = Validasi.wajib(nama, "Nama wajib diisi.");
        if (namaBersih.length() > 60) {
            throw new IllegalArgumentException("Nama terlalu panjang.");
        }
        String akun = username == null ? "" : username.trim();
        if (!akun.matches("[A-Za-z0-9._]{3,30}")) {
            throw new IllegalArgumentException("Username 3-30 karakter, hanya huruf, angka, titik, atau garis bawah.");
        }
        if (data.usernameDipakai(akun, admin.getIdPengguna())) {
            throw new IllegalArgumentException("Username sudah dipakai.");
        }
        String foto = admin.getFoto();
        if (hapusFoto) {
            foto = null;
        } else if (fotoBaru != null) {
            foto = salinFoto(fotoBaru);
        }
        admin.setNama(namaBersih);
        admin.setUsername(akun);
        admin.setFoto(foto);
        tulis(admin);
    }

    private String salinFoto(Path sumber) {
        String nama = sumber.getFileName().toString().toLowerCase(Locale.ROOT);
        String ekstensi = nama.endsWith(".png") ? ".png" : nama.endsWith(".jpeg") || nama.endsWith(".jpg") ? ".jpg" : "";
        if (ekstensi.isEmpty()) {
            throw new IllegalArgumentException("Foto harus berupa PNG atau JPG.");
        }
        Path tujuan = Path.of("data", "foto-admin" + ekstensi);
        try {
            Files.createDirectories(tujuan.getParent());
            Files.copy(sumber, tujuan, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Foto tidak bisa disimpan.");
        }
        Path lawan = Path.of("data", "foto-admin" + (ekstensi.equals(".png") ? ".jpg" : ".png"));
        try {
            Files.deleteIfExists(lawan);
        } catch (IOException ex) {
            // berkas lama tidak menghalangi foto yang baru
        }
        return tujuan.toAbsolutePath().toString();
    }

    private void tulis(Admin admin) {
        Properties isi = new Properties();
        isi.setProperty("nama", admin.getNama());
        isi.setProperty("username", admin.getUsername());
        isi.setProperty("foto", admin.getFoto() == null ? "" : admin.getFoto());
        try {
            Files.createDirectories(BERKAS.getParent());
            try (OutputStream keluar = Files.newOutputStream(BERKAS)) {
                isi.store(keluar, "profil admin");
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Perubahan profil tidak bisa disimpan.");
        }
    }
}
