package com.tiketin.model;

public class Film {
    private int idFilm;
    private String judul;
    private String genre;
    private int durasi;
    private String sinopsis;
    private int idAdmin;

    public Film(int idFilm, String judul, String genre, int durasi, String sinopsis, int idAdmin) {
        this.idFilm = idFilm;
        this.judul = judul;
        this.genre = genre;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.idAdmin = idAdmin;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurasi() {
        return durasi;
    }

    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}
