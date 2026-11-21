package com.tiketin.model;

public class KasirTiketItem {

    private int no;
    private String kursi;
    private String kodeTiket;
    private int harga;

    public KasirTiketItem(int no, String kursi, 
                         String kodeTiket, int harga) {
        this.no = no;
        this.kursi = kursi;
        this.kodeTiket = kodeTiket;
        this.harga = harga;
    }

    public int getNo() {
        return no;
    }

    public String getKursi() {
        return kursi;
    }

    public String getKodeTiket() {
        return kodeTiket;
    }

    public int getHarga() {
        return harga;
    }
}