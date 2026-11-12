package com.tiketin.model;

public class Admin extends Pengguna {
    private int idAdmin;

    public Admin(){
        super();
    }

    public Admin(int idAdmin, int idPengguna, String username, String password, String nama){
        super(idPengguna, username, password, nama);
        this.idAdmin = idAdmin;
    }

    @Override 
    public String getPeran(){
        return "ADMIN";
    }

    public int getIdAdmin() {
        return idAdmin;
    }
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
