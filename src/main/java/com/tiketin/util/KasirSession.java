package com.tiketin.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kasir;
import com.tiketin.model.Kursi;

public class KasirSession {

    private static Kasir kasirLogin;
    private static Film filmTerpilih;
    private static LocalDate tanggalTerpilih;
    private static String jamTerpilih;
    private static String studioTerpilih;
    private static JadwalTayang jadwalTerpilih;
    private static List<Kursi> kursiTerpilih = new ArrayList<>();
    private static Integer idPemesananTerakhir;

    public static void setKasirLogin(Kasir kasir) {
        kasirLogin = kasir;
    }

    public static Kasir getKasirLogin() {
        return kasirLogin;
    }

    public static void setFilmTerpilih(Film film) {
        filmTerpilih = film;
    }

    public static Film getFilmTerpilih() {
        return filmTerpilih;
    }

    public static void setJamTerpilih(String jam) {
        jamTerpilih = jam;
    }

    public static String getJamTerpilih() {
        return jamTerpilih;
    }

    public static void setStudioTerpilih(String studio) {
        studioTerpilih = studio;
    }

    public static String getStudioTerpilih() {
        return studioTerpilih;
    }

    public static void setTanggalTerpilih(LocalDate tanggal) {
        tanggalTerpilih = tanggal;
    }

    public static LocalDate getTanggalTerpilih() {
        return tanggalTerpilih;
    }

    // Jadwal yang dipilih (dari database)
    public static void setJadwalTerpilih(JadwalTayang jadwal) {
        jadwalTerpilih = jadwal;
    }

    public static JadwalTayang getJadwalTerpilih() {
        return jadwalTerpilih;
    }

    // Kursi yang dipilih di denah
    public static void setKursiTerpilih(List<Kursi> kursi) {
        kursiTerpilih = kursi == null ? new ArrayList<>() : new ArrayList<>(kursi);
    }

    public static List<Kursi> getKursiTerpilih() {
        return new ArrayList<>(kursiTerpilih);
    }

    // Total bayar = harga jadwal x jumlah kursi
    public static double getTotalBayar() {
        if (jadwalTerpilih == null) {
            return 0;
        }
        return jadwalTerpilih.getHargaTiket() * kursiTerpilih.size();
    }

    // Id pemesanan terakhir, dipakai halaman Tiket
    public static void setIdPemesananTerakhir(Integer idPemesanan) {
        idPemesananTerakhir = idPemesanan;
    }

    public static Integer getIdPemesananTerakhir() {
        return idPemesananTerakhir;
    }

    // Reset pilihan transaksi saja, kasir tetap login
    public static void reset() {
        filmTerpilih = null;
        tanggalTerpilih = null;
        jamTerpilih = null;
        studioTerpilih = null;
        jadwalTerpilih = null;
        kursiTerpilih = new ArrayList<>();
        idPemesananTerakhir = null;
    }

    public static void logout() {
        reset();
        kasirLogin = null;
    }
}
