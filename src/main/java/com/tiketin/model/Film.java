package com.tiketin.model;

public class Film {

    private String judul;
    private String genre;
    private int durasi;
    private String sinopsis;
    private String poster;

    public Film(String judul, String genre, int durasi,
                String sinopsis, String poster) {
        this.judul = judul;
        this.genre = genre;
        this.durasi = durasi;
        this.sinopsis = sinopsis;
        this.poster = poster;
    }

    public String getJudul() {
        return judul;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurasi() {
        return durasi;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getPoster() {
        return poster;
    }
}
