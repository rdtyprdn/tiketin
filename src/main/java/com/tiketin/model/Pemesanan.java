package com.tiketin.model;

import java.time.LocalDateTime;

import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusPemesanan;

public class Pemesanan {
    private int idPemesanan;
    private int idJadwal;
    private Integer idKasir;
    private int idPelanggan;
    private LocalDateTime waktuPemesanan;
    private KanalPemesanan kanalPemesanan;
    private StatusPemesanan statusPemesanan;
    private int totalBayar;

    public Pemesanan(int idPemesanan, int idJadwal, Integer idKasir, int idPelanggan,
                     LocalDateTime waktuPemesanan, KanalPemesanan kanalPemesanan,
                     StatusPemesanan statusPemesanan, int totalBayar) {
        this.idPemesanan = idPemesanan;
        this.idJadwal = idJadwal;
        this.idKasir = idKasir;
        this.idPelanggan = idPelanggan;
        this.waktuPemesanan = waktuPemesanan;
        this.kanalPemesanan = kanalPemesanan;
        this.statusPemesanan = statusPemesanan;
        this.totalBayar = totalBayar;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public Integer getIdKasir() {
        return idKasir;
    }

    public void setIdKasir(Integer idKasir) {
        this.idKasir = idKasir;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(int idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public LocalDateTime getWaktuPemesanan() {
        return waktuPemesanan;
    }

    public void setWaktuPemesanan(LocalDateTime waktuPemesanan) {
        this.waktuPemesanan = waktuPemesanan;
    }

    public KanalPemesanan getKanalPemesanan() {
        return kanalPemesanan;
    }

    public void setKanalPemesanan(KanalPemesanan kanalPemesanan) {
        this.kanalPemesanan = kanalPemesanan;
    }

    public StatusPemesanan getStatusPemesanan() {
        return statusPemesanan;
    }

    public void setStatusPemesanan(StatusPemesanan statusPemesanan) {
        this.statusPemesanan = statusPemesanan;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }
}
