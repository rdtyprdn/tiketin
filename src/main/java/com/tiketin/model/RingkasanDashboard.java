package com.tiketin.model;

import java.util.List;

public class RingkasanDashboard {
    private final int jumlahFilm;
    private final int jumlahJadwalAktif;
    private final int jumlahTransaksi;
    private final int pendapatan;
    private final List<TitikGrafik> grafik;

    public RingkasanDashboard(int jumlahFilm, int jumlahJadwalAktif, int jumlahTransaksi,
                              int pendapatan, List<TitikGrafik> grafik) {
        this.jumlahFilm = jumlahFilm;
        this.jumlahJadwalAktif = jumlahJadwalAktif;
        this.jumlahTransaksi = jumlahTransaksi;
        this.pendapatan = pendapatan;
        this.grafik = List.copyOf(grafik);
    }

    public int getJumlahFilm() {
        return jumlahFilm;
    }

    public int getJumlahJadwalAktif() {
        return jumlahJadwalAktif;
    }

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public int getPendapatan() {
        return pendapatan;
    }

    public List<TitikGrafik> getGrafik() {
        return grafik;
    }
}
