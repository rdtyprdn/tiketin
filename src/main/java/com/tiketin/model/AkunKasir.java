package com.tiketin.model;

public class AkunKasir {
    private final int idKasir;
    private final int idPengguna;
    private final String nama;
    private final String username;

    public AkunKasir(int idKasir, int idPengguna, String nama, String username) {
        this.idKasir = idKasir;
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.username = username;
    }

    public int getIdKasir() {
        return idKasir;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }
}

