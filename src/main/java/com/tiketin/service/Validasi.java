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

    // Nominal rupiah dari teks form. Menerima 50000, 50.000, 50,000, 50000.00, 50000,00,
    // Rp 50.000, dan juga format double Java seperti 50000.0 atau 5.0E7.
    static int uang(String nilai, String pesanFormat) {
        if (nilai == null || nilai.isBlank()) {
            throw new IllegalArgumentException(pesanFormat);
        }
        String teks = nilai.trim().replaceAll("(?i)^rp\\.?", "").replaceAll("[\\s_]", "");
        String angka;
        if (teks.matches("\\d+(\\.\\d+)?[eE][+-]?\\d+")) {
            angka = teks;                                              // 5.0E7
        } else if (teks.matches("\\d{1,3}(\\.\\d{3})+(,\\d+)?")) {
            angka = teks.replace(".", "").replace(',', '.');           // 50.000 / 50.000,00
        } else if (teks.matches("\\d{1,3}(,\\d{3})+(\\.\\d+)?")) {
            angka = teks.replace(",", "");                             // 50,000 / 50,000.00
        } else if (teks.matches("\\d+([.,]\\d+)?")) {
            angka = teks.replace(',', '.');                            // 50000 / 50000.0 / 50000,00
        } else {
            throw new IllegalArgumentException(pesanFormat);
        }
        try {
            java.math.BigDecimal hasil = new java.math.BigDecimal(angka).setScale(0, java.math.RoundingMode.HALF_UP);
            return hasil.intValueExact();
        } catch (ArithmeticException | NumberFormatException ex) {
            throw new IllegalArgumentException(pesanFormat);
        }
    }
}
