package com.tiketin.model;

public class Studio {
    private int idStudio;
    private String namaStudio;
    private int idAdmin;

    public Studio(int idStudio, String namaStudio, int idAdmin) {
        this.idStudio = idStudio;
        this.namaStudio = namaStudio;
        this.idAdmin = idAdmin;
    }

    public int getIdStudio() {
        return idStudio;
    }

    public void setIdStudio(int idStudio) {
        this.idStudio = idStudio;
    }

    public String getNamaStudio() {
        return namaStudio;
    }

    public void setNamaStudio(String namaStudio) {
        this.namaStudio = namaStudio;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
