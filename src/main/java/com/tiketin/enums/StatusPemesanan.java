package com.tiketin.enums;

public enum StatusPemesanan {
    MENUNGGU_PEMBAYARAN("Menunggu pembayaran"),
    LUNAS("Lunas"),
    DIBATALKAN("Dibatalkan");

    private final String label;

    StatusPemesanan(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
