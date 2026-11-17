package com.tiketin.model;

import java.time.LocalDateTime;

public class JadwalTayang {
    private int idJadwal;
    private int idFilm;
    private int idStudio;
    private LocalDateTime waktuMulai;
    private double hargaTiket;
    private int idAdmin;

    public JadwalTayang(){

    }

    public JadwalTayang(int idJadwal, int idFilm, int idStudio, LocalDateTime waktuMulai, double hargaTiket, int idAdmin) {
        this.idJadwal = idJadwal;
        this.idFilm = idFilm;
        this.idStudio = idStudio;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.idAdmin = idAdmin;
    }

    public int getIdJadwal() {
        return idJadwal;
    }
    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }
    public int getIdFilm() {
        return idFilm;
    }
    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }
    public int getIdStudio() {
        return idStudio;
    }
    public void setIdStudio(int idStudio) {
        this.idStudio = idStudio;
    }
    public LocalDateTime getWaktuMulai() {
        return waktuMulai;
    }
    public void setWaktuMulai(LocalDateTime waktuMulai) {
        this.waktuMulai = waktuMulai;
    }
    public double getHargaTiket() {
        return hargaTiket;
    }
    public void setHargaTiket(double hargaTiket) {
        this.hargaTiket = hargaTiket;
    }
    public int getIdAdmin() {
        return idAdmin;
    }
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
