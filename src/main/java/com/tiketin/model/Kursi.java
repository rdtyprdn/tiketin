package com.tiketin.model;

public class Kursi {
    private int idKursi;
    private int idStudio;
    private String nomorKursi;
    private String tipeKursi;
    private int idAdmin;

    public Kursi(int idKursi, int idStudio, String nomorKursi, String tipeKursi, int idAdmin) {
        this.idKursi = idKursi;
        this.idStudio = idStudio;
        this.nomorKursi = nomorKursi;
        this.tipeKursi = tipeKursi;
        this.idAdmin = idAdmin;
    }

    public int getIdKursi() {
        return idKursi;
    }

    public void setIdKursi(int idKursi) {
        this.idKursi = idKursi;
    }

    public int getIdStudio() {
        return idStudio;
    }

    public void setIdStudio(int idStudio) {
        this.idStudio = idStudio;
    }

    public String getNomorKursi() {
        return nomorKursi;
    }

    public void setNomorKursi(String nomorKursi) {
        this.nomorKursi = nomorKursi;
    }

    public String getTipeKursi() {
        return tipeKursi;
    }

    public void setTipeKursi(String tipeKursi) {
        this.tipeKursi = tipeKursi;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
