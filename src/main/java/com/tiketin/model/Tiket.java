package com.tiketin.model;

import java.time.LocalDateTime;

public class Tiket {
    private int idTiket;
    private int idDetail;
    private String kodeTiket;
    private LocalDateTime tanggalTerbit;
    private String statusTiket;

    public Tiket(){

    }

    public Tiket(int idTiket, int idDetail, String kodeTiket, LocalDateTime tanggalTerbit, String statusTiket) {
        this.idTiket = idTiket;
        this.idDetail = idDetail;
        this.kodeTiket = kodeTiket;
        this.tanggalTerbit = tanggalTerbit;
        this.statusTiket = statusTiket;
    }

    public int getIdTiket() {
        return idTiket;
    }
    public void setIdTiket(int idTiket) {
        this.idTiket = idTiket;
    }
    public int getIdDetail() {
        return idDetail;
    }
    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }
    public String getKodeTiket() {
        return kodeTiket;
    }
    public void setKodeTiket(String kodeTiket) {
        this.kodeTiket = kodeTiket;
    }
    public LocalDateTime getTanggalTerbit() {
        return tanggalTerbit;
    }
    public void setTanggalTerbit(LocalDateTime tanggalTerbit) {
        this.tanggalTerbit = tanggalTerbit;
    }
    public String getStatusTiket() {
        return statusTiket;
    }
    public void setStatusTiket(String statusTiket) {
        this.statusTiket = statusTiket;
    }
}
