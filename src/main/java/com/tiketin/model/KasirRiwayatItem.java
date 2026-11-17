package com.tiketin.model;

import java.time.LocalDateTime;

public class KasirRiwayatItem {

    private String idTransaksi;
    private LocalDateTime tanggal;
    private String film;
    private int jumlahTiket;
    private int total;
    private String status;

    public KasirRiwayatItem(String idTransaksi,
                            LocalDateTime tanggal,
                            String film,
                            int jumlahTiket,
                            int total,
                            String status) {

        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.film = film;
        this.jumlahTiket = jumlahTiket;
        this.total = total;
        this.status = status;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public String getFilm() {
        return film;
    }

    public int getJumlahTiket() {
        return jumlahTiket;
    }

    public int getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }
}