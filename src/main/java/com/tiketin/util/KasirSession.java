package com.tiketin.util;

import java.time.LocalDate;

import com.tiketin.model.Film;

public class KasirSession {

    private static Film filmTerpilih;
    private static LocalDate tanggalTerpilih;
    private static String jamTerpilih;
    private static String studioTerpilih;

    public static void setFilmTerpilih(Film film) {
        filmTerpilih = film;
    }

    public static Film getFilmTerpilih() {
        return filmTerpilih;
    }
    
    public static void setJamTerpilih(String jam) {
    jamTerpilih = jam;
    }

    public static String getJamTerpilih() {
    return jamTerpilih;
    }

    public static void setStudioTerpilih(String studio) {
    studioTerpilih = studio;
    }

    public static String getStudioTerpilih() {
    return studioTerpilih;
    }

    public static void reset() {
    filmTerpilih = null;
    tanggalTerpilih = null; 
    jamTerpilih = null;
    studioTerpilih = null;
    }

    public static void setTanggalTerpilih(LocalDate tanggal) {
    tanggalTerpilih = tanggal;
    }

    public static LocalDate getTanggalTerpilih() {
    return tanggalTerpilih;
    }
}
