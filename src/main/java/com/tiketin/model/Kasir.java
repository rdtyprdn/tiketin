package com.tiketin.model;

public class Kasir extends Pengguna{
    private int idKasir;

    public Kasir(){
        super();
    }

    public Kasir(int idKasir, int idPengguna, String username, String password, String nama) {
        super(idPengguna, username, password, nama);
        this.idKasir = idKasir;
    }

    @Override
    public String getPeran() {
        return "KASIR";
    }

    public int getIdKasir() {
        return idKasir;
    }
    public void setIdKasir(int idKasir) {
        this.idKasir = idKasir;
    }
}
