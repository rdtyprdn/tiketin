package com.tiketin.model;

import java.time.LocalDateTime;

import com.tiketin.enums.StatusJadwal;

public class JadwalTayang {
    private int idJadwal;
    private int idFilm;
    private int idStudio;
    private LocalDateTime waktuMulai;
    private int hargaTiket;
    private StatusJadwal statusJadwal;
    private int idAdmin;

    public JadwalTayang(int idJadwal, int idFilm, int idStudio, LocalDateTime waktuMulai,
                        int hargaTiket, StatusJadwal statusJadwal, int idAdmin) {
        this.idJadwal = idJadwal;
        this.idFilm = idFilm;
        this.idStudio = idStudio;
        this.waktuMulai = waktuMulai;
        this.hargaTiket = hargaTiket;
        this.statusJadwal = statusJadwal;
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

    public int getHargaTiket() {
        return hargaTiket;
    }

    public void setHargaTiket(int hargaTiket) {
        this.hargaTiket = hargaTiket;
    }

    public StatusJadwal getStatusJadwal() {
        return statusJadwal;
    }

    public void setStatusJadwal(StatusJadwal statusJadwal) {
        this.statusJadwal = statusJadwal;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
