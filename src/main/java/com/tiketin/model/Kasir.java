package com.tiketin.model;

public class Kasir extends Pengguna {
    private int idKasir;

    public Kasir(int idPengguna, String username, String password, String nama, int idKasir) {
        super(idPengguna, username, password, nama);
        this.idKasir = idKasir;
    }

    @Override
    public String peran() {
        return "KASIR";
    }

    public int getIdKasir() {
        return idKasir;
    }

    public void setIdKasir(int idKasir) {
        this.idKasir = idKasir;
    }
}
