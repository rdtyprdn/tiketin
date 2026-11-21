package com.tiketin.service;

import com.tiketin.model.Film;
import com.tiketin.util.PosterUtil;

import java.nio.file.Path;

import java.util.List;
import java.util.Locale;

public class FilmService {
    // Pilihan rating usia (klasifikasi LSF)
    public static final List<String> RATING_USIA = List.of("SU", "13+", "17+", "21+");

    // Isian detail film dari form admin (semua berupa teks apa adanya; boleh kosong)
    public record Detail(String sutradara, String tahunRilis, String ratingUsia, String rating) {
    }

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
        return tambah(judul, genre, durasiTeks, sinopsis, null, idAdmin, posterBaru);
    }

    // detail boleh null: sutradara/tahun/rating kosong, rating usia SU
    public Film tambah(String judul, String genre, String durasiTeks, String sinopsis, Detail detail,
                       int idAdmin, Path posterBaru) {
        Film film = susun(0, judul, genre, durasiTeks, sinopsis, idAdmin);
        terapkanDetail(film, detail, null);
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
        return ubah(idFilm, judul, genre, durasiTeks, sinopsis, null, posterBaru, hapusPoster);
    }

    // detail null = detail lama (sutradara, tahun, rating usia, rating) dipertahankan
    public Film ubah(int idFilm, String judul, String genre, String durasiTeks, String sinopsis, Detail detail,
                     Path posterBaru, boolean hapusPoster) {
        Film lama = wajibAda(idFilm);
        Film baru = susun(idFilm, judul, genre, durasiTeks, sinopsis, lama.getIdAdmin());
        terapkanDetail(baru, detail, lama);
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

    private void terapkanDetail(Film film, Detail detail, Film lama) {
        if (detail == null) {
            if (lama != null) {
                film.setSutradara(lama.getSutradara());
                film.setTahunRilis(lama.getTahunRilis());
                film.setRatingUsia(lama.getRatingUsia());
                film.setRating(lama.getRating());
            } else {
                film.setRatingUsia("SU");
            }
            return;
        }
        film.setSutradara(sutradara(detail.sutradara()));
        film.setTahunRilis(tahunRilis(detail.tahunRilis()));
        film.setRatingUsia(ratingUsia(detail.ratingUsia()));
        film.setRating(rating(detail.rating()));
    }

    // Kosong boleh; maksimal 150 karakter
    static String sutradara(String teks) {
        if (teks == null || teks.isBlank()) {
            return null;
        }
        String bersih = teks.trim().replaceAll("\\s+", " ");
        if (bersih.length() > 150) {
            throw new IllegalArgumentException("Nama sutradara maksimal 150 karakter.");
        }
        return bersih;
    }

    // Kosong boleh; kalau diisi: 1900 sampai tahun ini + 2 (film yang akan datang)
    static Integer tahunRilis(String teks) {
        if (teks == null || teks.isBlank()) {
            return null;
        }
        int maks = java.time.Year.now().getValue() + 2;
        String bersih = teks.trim();
        if (!bersih.matches("\\d{4}")) {
            throw new IllegalArgumentException("Tahun rilis harus 4 digit angka, contoh 2025.");
        }
        int tahun = Integer.parseInt(bersih);
        if (tahun < 1900 || tahun > maks) {
            throw new IllegalArgumentException("Tahun rilis harus antara 1900 dan " + maks + ".");
        }
        return tahun;
    }

    // Kosong -> SU; selain itu harus salah satu dari SU, 13+, 17+, 21+
    static String ratingUsia(String teks) {
        if (teks == null || teks.isBlank()) {
            return "SU";
        }
        String bersih = teks.trim().toUpperCase(Locale.ROOT).replace(" ", "");
        if (bersih.matches("\\d{2}")) {
            bersih = bersih + "+";
        }
        if (!RATING_USIA.contains(bersih)) {
            throw new IllegalArgumentException("Rating usia harus salah satu dari SU, 13+, 17+, 21+.");
        }
        return bersih;
    }

    // Kosong boleh; 1 sampai 5 (bintang), koma atau titik (4,5 atau 4.5), dibulatkan 1 desimal
    static Double rating(String teks) {
        if (teks == null || teks.isBlank()) {
            return null;
        }
        String pesan = "Rating harus angka 1 sampai 5, contoh 4,5 atau 4.5.";
        String bersih = teks.trim().replace(',', '.');
        if (!bersih.matches("\\d(\\.\\d+)?")) {
            throw new IllegalArgumentException(pesan);
        }
        java.math.BigDecimal nilai = new java.math.BigDecimal(bersih).setScale(1, java.math.RoundingMode.HALF_UP);
        if (nilai.compareTo(java.math.BigDecimal.ONE) < 0 || nilai.compareTo(java.math.BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException(pesan);
        }
        return nilai.doubleValue();
    }

    // Tampilan rating "4.5" (atau "-" kalau kosong); dipakai admin dan kiosk
    public static String formatRating(Double rating) {
        return rating == null ? "-" : String.format(Locale.ROOT, "%.1f", rating);
    }

    private Film wajibAda(int idFilm) {
        Film film = data.cariFilm(idFilm);
        if (film == null) {
            throw new IllegalArgumentException("Film tidak ditemukan.");
        }
        return film;
    }
}
