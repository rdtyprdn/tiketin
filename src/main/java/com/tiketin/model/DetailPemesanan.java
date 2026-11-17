package com.tiketin.model;

public class DetailPemesanan {
    private int idDetail;
    private int idPemesanan;
    private int idJadwal;
    private int idKursi;
    private double hargaTiket;

    public DetailPemesanan(){

    }

    public DetailPemesanan(int idDetail, int idPemesanan, int idJadwal, int idKursi, double hargaTiket){
        this.idDetail = idDetail;
        this.idPemesanan = idPemesanan;
        this.idJadwal = idJadwal;
        this.idKursi = idKursi;
        this.hargaTiket = hargaTiket;
    }

    public int getIdDetail() {
        return idDetail;
    }
    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
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
    public int getIdKursi() {
        return idKursi;
    }
    public void setIdKursi(int idKursi) {
        this.idKursi = idKursi;
    }
    public double getHargaTiket() {
        return hargaTiket;
    }
    public void setHargaTiket(double hargaTiket) {
        this.hargaTiket = hargaTiket;
    }
}
