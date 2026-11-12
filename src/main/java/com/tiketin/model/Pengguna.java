package com.tiketin.model;

public abstract class Pengguna {
    private int idPengguna;
    private String username;
    private String password;
    private String nama;

    public Pengguna(){

    }

    public Pengguna(int idPengguna, String username, String password, String nama){
        this.idPengguna = idPengguna;
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    public abstract String getPeran();

    public int getIdPengguna() {
        return idPengguna;
    }
    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    
}
