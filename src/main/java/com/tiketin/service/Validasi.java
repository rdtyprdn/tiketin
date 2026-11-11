package com.tiketin.service;

final class Validasi {
    private Validasi() {
    }

    static String wajib(String nilai, String pesan) {
        if (nilai == null || nilai.isBlank()) {
            throw new IllegalArgumentException(pesan);
        }
        return nilai.trim();
    }

    static int angka(String nilai, String pesanFormat) {
        if (nilai == null || nilai.isBlank()) {
            throw new IllegalArgumentException(pesanFormat);
        }
        try {
            return Integer.parseInt(nilai.trim().replace(".", ""));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(pesanFormat);
        }
    }
}
