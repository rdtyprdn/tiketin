package com.tiketin.service;

import com.tiketin.model.Film;

import java.util.List;
import java.util.Locale;

public class FilmService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public List<Film> cari(String kata) {
        String kueri = kata == null ? "" : kata.trim().toLowerCase(Locale.ROOT);
        return data.film().stream()
                .filter(film -> kueri.isEmpty()
                        || film.getJudul().toLowerCase(Locale.ROOT).contains(kueri)
                        || film.getGenre().toLowerCase(Locale.ROOT).contains(kueri))
                .toList();
    }

    public Film tambah(String judul, String genre, String durasiTeks, String sinopsis, int idAdmin) {
        return data.simpanFilm(susun(0, judul, genre, durasiTeks, sinopsis, idAdmin));
    }

    public Film ubah(int idFilm, String judul, String genre, String durasiTeks, String sinopsis) {
        Film lama = wajibAda(idFilm);
        Film baru = susun(idFilm, judul, genre, durasiTeks, sinopsis, lama.getIdAdmin());
        return data.simpanFilm(baru);
    }

    public void hapus(int idFilm) {
        wajibAda(idFilm);
        boolean dipakai = data.jadwal().stream().anyMatch(jadwal -> jadwal.getIdFilm() == idFilm);
        if (dipakai) {
            throw new IllegalStateException("Film tidak bisa dihapus karena masih punya jadwal tayang.");
        }
        data.hapusFilm(idFilm);
    }

    private Film susun(int idFilm, String judul, String genre, String durasiTeks, String sinopsis, int idAdmin) {
        String judulBersih = Validasi.wajib(judul, "Judul wajib diisi.");
        String genreBersih = Validasi.wajib(genre, "Genre wajib diisi.");
        String sinopsisBersih = Validasi.wajib(sinopsis, "Sinopsis wajib diisi.");
        int durasi = Validasi.angka(durasiTeks, "Durasi harus berupa angka menit.");
        if (durasi < 1 || durasi > 400) {
            throw new IllegalArgumentException("Durasi harus antara 1 dan 400 menit.");
        }
        return new Film(idFilm, judulBersih, genreBersih, durasi, sinopsisBersih, idAdmin);
    }

    private Film wajibAda(int idFilm) {
        Film film = data.cariFilm(idFilm);
        if (film == null) {
            throw new IllegalArgumentException("Film tidak ditemukan.");
        }
        return film;
    }
}
