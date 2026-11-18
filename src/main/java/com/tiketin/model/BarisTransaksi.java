package com.tiketin.model;

import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusPemesanan;

import java.time.LocalDateTime;

public class BarisTransaksi {
    private final int idPemesanan;
    private final int idJadwal;
    private final LocalDateTime waktu;
    private final KanalPemesanan kanal;
    private final String namaKasir;
    private final int totalBayar;
    private final StatusPemesanan status;
    private final String judulFilm;
    private final String namaStudio;
    private final String waktuTayang;
    private final String kursi;
    private final int jumlahKursi;

    public BarisTransaksi(int idPemesanan, int idJadwal, LocalDateTime waktu, KanalPemesanan kanal,
                          String namaKasir, int totalBayar, StatusPemesanan status, String judulFilm,
                          String namaStudio, String waktuTayang, String kursi, int jumlahKursi) {
        this.idPemesanan = idPemesanan;
        this.idJadwal = idJadwal;
        this.waktu = waktu;
        this.kanal = kanal;
        this.namaKasir = namaKasir;
        this.totalBayar = totalBayar;
        this.status = status;
        this.judulFilm = judulFilm;
        this.namaStudio = namaStudio;
        this.waktuTayang = waktuTayang;
        this.kursi = kursi;
        this.jumlahKursi = jumlahKursi;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public int getIdJadwal() {
        return idJadwal;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public KanalPemesanan getKanal() {
        return kanal;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public StatusPemesanan getStatus() {
        return status;
    }

    public String getJudulFilm() {
        return judulFilm;
    }

    public String getNamaStudio() {
        return namaStudio;
    }

    public String getWaktuTayang() {
        return waktuTayang;
    }

    public String getKursi() {
        return kursi;
    }

    public int getJumlahKursi() {
        return jumlahKursi;
    }
}
