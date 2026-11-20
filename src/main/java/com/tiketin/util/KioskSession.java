package com.tiketin.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kursi;

// Menyimpan pilihan pelanggan selama satu kali pemesanan di kiosk
// (mirip KasirSession, tapi tanpa login)
public final class KioskSession {

    private static Film filmTerpilih;
    private static LocalDate tanggalTerpilih;
    private static JadwalTayang jadwalTerpilih;
    private static String studioTerpilih;
    private static List<Kursi> kursiTerpilih = new ArrayList<>();
    private static Integer idPemesananTerakhir;

    private KioskSession() {
    }

    public static Film getFilmTerpilih() {
        return filmTerpilih;
    }

    public static void setFilmTerpilih(Film film) {
        // ganti film = pilihan jadwal & kursi sebelumnya tidak berlaku lagi
        if (filmTerpilih == null || film == null || filmTerpilih.getIdFilm() != film.getIdFilm()) {
            tanggalTerpilih = null;
            jadwalTerpilih = null;
            studioTerpilih = null;
            kursiTerpilih = new ArrayList<>();
        }
        filmTerpilih = film;
    }

    public static LocalDate getTanggalTerpilih() {
        return tanggalTerpilih;
    }

    public static void setTanggalTerpilih(LocalDate tanggal) {
        tanggalTerpilih = tanggal;
    }

    public static JadwalTayang getJadwalTerpilih() {
        return jadwalTerpilih;
    }

    public static void setJadwalTerpilih(JadwalTayang jadwal, String namaStudio) {
        jadwalTerpilih = jadwal;
        studioTerpilih = namaStudio;
    }

    public static String getStudioTerpilih() {
        return studioTerpilih;
    }

    public static List<Kursi> getKursiTerpilih() {
        return new ArrayList<>(kursiTerpilih);
    }

    public static void setKursiTerpilih(List<Kursi> kursi) {
        kursiTerpilih = kursi == null ? new ArrayList<>() : new ArrayList<>(kursi);
    }

    // Total bayar = harga jadwal x jumlah kursi
    public static double getTotalBayar() {
        return jadwalTerpilih == null ? 0 : jadwalTerpilih.getHargaTiket() * kursiTerpilih.size();
    }

    public static Integer getIdPemesananTerakhir() {
        return idPemesananTerakhir;
    }

    public static void setIdPemesananTerakhir(Integer idPemesanan) {
        idPemesananTerakhir = idPemesanan;
    }

    // Dipanggil di halaman awal kiosk, supaya pelanggan berikutnya mulai dari nol
    public static void reset() {
        filmTerpilih = null;
        tanggalTerpilih = null;
        jadwalTerpilih = null;
        studioTerpilih = null;
        kursiTerpilih = new ArrayList<>();
        idPemesananTerakhir = null;
    }
}
