package com.tiketin.model;

public class Admin extends Pengguna {
    private int idAdmin;

    public Admin(int idPengguna, String username, String password, String nama, int idAdmin) {
        super(idPengguna, username, password, nama);
        this.idAdmin = idAdmin;
    }

    @Override
    public String peran() {
        return "ADMIN";
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
