package com.tiketin.service;

import com.tiketin.model.AkunKasir;
import com.tiketin.model.Kasir;
import com.tiketin.util.PasswordUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class KasirService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public List<AkunKasir> cari(String kata) {
        String kueri = kata == null ? "" : kata.trim().toLowerCase(Locale.ROOT);
        return data.pengguna().stream()
                .filter(Kasir.class::isInstance)
                .map(Kasir.class::cast)
                .filter(kasir -> kueri.isEmpty()
                        || kasir.getNama().toLowerCase(Locale.ROOT).contains(kueri)
                        || kasir.getUsername().toLowerCase(Locale.ROOT).contains(kueri))
                .sorted(Comparator.comparingInt(Kasir::getIdKasir))
                .map(kasir -> new AkunKasir(kasir.getIdKasir(), kasir.getIdPengguna(), kasir.getNama(), kasir.getUsername()))
                .toList();
    }

    public Kasir tambah(String nama, String username, String password) {
        String namaBersih = Validasi.wajib(nama, "Nama kasir wajib diisi.");
        String usernameBersih = username(username, 0);
        String sandi = wajibPassword(password, true);
        Kasir kasir = new Kasir(0, usernameBersih, PasswordUtil.hash(sandi), namaBersih, 0);
        return data.simpanKasir(kasir);
    }

    public Kasir ubah(int idKasir, String nama, String username, String password) {
        Kasir lama = data.cariKasir(idKasir);
        if (lama == null) {
            throw new IllegalArgumentException("Kasir tidak ditemukan.");
        }
        String namaBersih = Validasi.wajib(nama, "Nama kasir wajib diisi.");
        String usernameBersih = username(username, lama.getIdPengguna());
        String sandi = wajibPassword(password, false);
        String hash = sandi == null ? lama.getPassword() : PasswordUtil.hash(sandi);
        Kasir baru = new Kasir(lama.getIdPengguna(), usernameBersih, hash, namaBersih, lama.getIdKasir());
        return data.simpanKasir(baru);
    }

    public void hapus(int idKasir) {
        if (data.cariKasir(idKasir) == null) {
            throw new IllegalArgumentException("Kasir tidak ditemukan.");
        }
        boolean menangani = data.pemesanan().stream()
                .anyMatch(pesanan -> idKasir == (pesanan.getIdKasir() == null ? -1 : pesanan.getIdKasir()));
        if (menangani) {
            throw new IllegalStateException("Kasir tidak bisa dihapus karena masih tercatat pada transaksi loket.");
        }
        data.hapusKasir(idKasir);
    }

    private String username(String username, int kecualiIdPengguna) {
        String bersih = Validasi.wajib(username, "Username wajib diisi.");
        if (bersih.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("Username tidak boleh mengandung spasi.");
        }
        if (data.usernameDipakai(bersih, kecualiIdPengguna)) {
            throw new IllegalArgumentException("Username sudah dipakai.");
        }
        return bersih;
    }

    private String wajibPassword(String password, boolean wajib) {
        if (password == null || password.isBlank()) {
            if (wajib) {
                throw new IllegalArgumentException("Password wajib diisi.");
            }
            return null;
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        }
        return password;
    }
}
