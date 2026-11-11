package com.tiketin.enums;

public enum StatusJadwal {
    AKTIF("Aktif"),
    DIBATALKAN("Dibatalkan");

    private final String label;

    StatusJadwal(String label) {
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
