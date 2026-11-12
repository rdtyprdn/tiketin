package com.tiketin.model;

import com.tiketin.enums.StatusPembayaran;

public class DataPembayaran {
    private int idPembayaran;
    private int idPemesanan;
    private String metodePembayaran;
    private StatusPembayaran statusPembayaran;
    private double jumlahBayar;
    private Double jumlahUangDiterima;
    private Double kembalian;

    public DataPembayaran(){

    }

       public DataPembayaran(int idPembayaran, int idPemesanan, String metodePembayaran,StatusPembayaran statusPembayaran, double jumlahBayar, Double jumlahUangDiterima, Double kembalian) {
        this.idPembayaran = idPembayaran;
        this.idPemesanan = idPemesanan;
        this.metodePembayaran = metodePembayaran;
        this.statusPembayaran = statusPembayaran;
        this.jumlahBayar = jumlahBayar;
        this.jumlahUangDiterima = jumlahUangDiterima;
        this.kembalian = kembalian;
    }

    public int getIdPembayaran() {
        return idPembayaran;
    }
    public void setIdPembayaran(int idPembayaran) {
        this.idPembayaran = idPembayaran;
    }
    public int getIdPemesanan() {
        return idPemesanan;
    }
    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }
    public String getMetodePembayaran() {
        return metodePembayaran;
    }
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
    public StatusPembayaran getStatusPembayaran() {
        return statusPembayaran;
    }
    public void setStatusPembayaran(StatusPembayaran statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }
    public double getJumlahBayar() {
        return jumlahBayar;
    }
    public void setJumlahBayar(double jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }
    public Double getJumlahUangDiterima() {
        return jumlahUangDiterima;
    }
    public void setJumlahUangDiterima(Double jumlahUangDiterima) {
        this.jumlahUangDiterima = jumlahUangDiterima;
    }
    public Double getKembalian() {
        return kembalian;
    }
    public void setKembalian(Double kembalian) {
        this.kembalian = kembalian;
    }

}
