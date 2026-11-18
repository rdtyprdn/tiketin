package com.tiketin.model;

public class TitikGrafik {
    private final String label;
    private final int nilai;
    private final int jumlah;

    public TitikGrafik(String label, int nilai) {
        this(label, nilai, 0);
    }

    public TitikGrafik(String label, int nilai, int jumlah) {
        this.label = label;
        this.nilai = nilai;
        this.jumlah = jumlah;
    }

    public String getLabel() {
        return label;
    }

    public int getNilai() {
        return nilai;
    }

    public int getJumlah() {
        return jumlah;
    }
}
