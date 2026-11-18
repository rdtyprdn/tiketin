package com.tiketin.enums;

public enum KanalPemesanan {
    KIOSK("Kiosk"),
    KASIR("Kasir");

    private final String label;

    KanalPemesanan(String label) {
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
