package com.tiketin.service;

import com.tiketin.model.Film;
import com.tiketin.util.PosterUtil;

import java.nio.file.Path;

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
        return tambah(judul, genre, durasiTeks, sinopsis, idAdmin, null);
    }

    // posterBaru boleh null (tanpa poster -> poster cadangan)
    public Film tambah(String judul, String genre, String durasiTeks, String sinopsis, int idAdmin, Path posterBaru) {
        Film film = susun(0, judul, genre, durasiTeks, sinopsis, idAdmin);
        if (posterBaru != null) {
            PosterUtil.bacaFile(posterBaru); // validasi dulu sebelum film disimpan
        }
        Film hasil = data.simpanFilm(film);
        if (posterBaru != null) {
            simpanPoster(film.getJudul(), posterBaru);
        }
        return hasil;
    }

    public Film ubah(int idFilm, String judul, String genre, String durasiTeks, String sinopsis) {
        return ubah(idFilm, judul, genre, durasiTeks, sinopsis, null, false);
    }

    // posterBaru: file pengganti (boleh null). hapusPoster: hapus poster yang ada (dipakai kalau posterBaru null).
    public Film ubah(int idFilm, String judul, String genre, String durasiTeks, String sinopsis,
                     Path posterBaru, boolean hapusPoster) {
        Film lama = wajibAda(idFilm);
        Film baru = susun(idFilm, judul, genre, durasiTeks, sinopsis, lama.getIdAdmin());
        if (posterBaru != null) {
            PosterUtil.bacaFile(posterBaru);
            posterBaru = salinSementara(posterBaru); // aman walau file yang dipilih adalah poster lama yang akan dipindah
        }
        Film hasil = data.simpanFilm(baru);
        try {
            // poster disimpan dengan nama = judul, jadi ikut pindah kalau judul berubah
            PosterUtil.gantiJudul(lama.getJudul(), baru.getJudul());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Film tersimpan, tapi poster lama gagal dipindah: " + ex.getMessage());
        }
        if (posterBaru != null) {
            simpanPoster(baru.getJudul(), posterBaru);
        } else if (hapusPoster) {
            PosterUtil.hapusPoster(baru.getJudul());
        }
        return hasil;
    }

    private static Path salinSementara(Path file) {
        try {
            String nama = file.getFileName().toString();
            Path tmp = java.nio.file.Files.createTempFile("tiketin-poster-", nama.substring(nama.lastIndexOf('.')));
            java.nio.file.Files.copy(file, tmp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            tmp.toFile().deleteOnExit();
            return tmp;
        } catch (java.io.IOException ex) {
            throw new IllegalArgumentException("File poster tidak bisa dibaca: " + ex.getMessage());
        }
    }

    private void simpanPoster(String judul, Path file) {
        try {
            PosterUtil.simpanPoster(judul, file);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Film tersimpan, tapi poster gagal disimpan: " + ex.getMessage());
        }
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
