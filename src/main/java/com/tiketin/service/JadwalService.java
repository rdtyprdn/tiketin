package com.tiketin.service;

import com.tiketin.enums.StatusJadwal;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Studio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class JadwalService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public List<JadwalTayang> cari(String kata) {
        String kueri = kata == null ? "" : kata.trim().toLowerCase(Locale.ROOT);
        return data.jadwal().stream()
                .filter(jadwal -> kueri.isEmpty() || judul(jadwal.getIdFilm()).toLowerCase(Locale.ROOT).contains(kueri)
                        || namaStudio(jadwal.getIdStudio()).toLowerCase(Locale.ROOT).contains(kueri))
                .toList();
    }

    public JadwalTayang tambah(int idFilm, int idStudio, LocalDate tanggal, LocalTime jam,
                               String hargaTeks, StatusJadwal status, int idAdmin) {
        return data.simpanJadwal(susun(0, idFilm, idStudio, tanggal, jam, hargaTeks, status, idAdmin));
    }

    public JadwalTayang ubah(int idJadwal, int idFilm, int idStudio, LocalDate tanggal, LocalTime jam,
                             String hargaTeks, StatusJadwal status) {
        JadwalTayang lama = wajibAda(idJadwal);
        return data.simpanJadwal(susun(idJadwal, idFilm, idStudio, tanggal, jam, hargaTeks, status, lama.getIdAdmin()));
    }

    public void hapus(int idJadwal) {
        wajibAda(idJadwal);
        boolean adaPesanan = data.pemesanan().stream().anyMatch(pesanan -> pesanan.getIdJadwal() == idJadwal);
        if (adaPesanan) {
            throw new IllegalStateException(
                    "Jadwal yang sudah memiliki pemesanan tidak dihapus. Ubah status menjadi Dibatalkan.");
        }
        data.hapusJadwal(idJadwal);
    }

    public String judul(int idFilm) {
        Film film = data.cariFilm(idFilm);
        return film == null ? "-" : film.getJudul();
    }

    public String namaStudio(int idStudio) {
        Studio studio = data.cariStudio(idStudio);
        return studio == null ? "-" : studio.getNamaStudio();
    }

    public List<Film> film() {
        return data.film();
    }

    public List<Studio> studio() {
        return data.studio();
    }

    private JadwalTayang susun(int idJadwal, int idFilm, int idStudio, LocalDate tanggal, LocalTime jam,
                               String hargaTeks, StatusJadwal status, int idAdmin) {
        if (data.cariFilm(idFilm) == null) {
            throw new IllegalArgumentException("Film wajib dipilih.");
        }
        if (data.cariStudio(idStudio) == null) {
            throw new IllegalArgumentException("Studio wajib dipilih.");
        }
        if (tanggal == null || jam == null) {
            throw new IllegalArgumentException("Tanggal dan jam tayang wajib diisi.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status jadwal wajib dipilih.");
        }
        int harga = Validasi.angka(hargaTeks, "Harga tiket harus berupa angka.");
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga tiket harus lebih dari 0.");
        }
        return new JadwalTayang(idJadwal, idFilm, idStudio, LocalDateTime.of(tanggal, jam), harga, status, idAdmin);
    }

    private JadwalTayang wajibAda(int idJadwal) {
        JadwalTayang jadwal = data.cariJadwal(idJadwal);
        if (jadwal == null) {
            throw new IllegalArgumentException("Jadwal tidak ditemukan.");
        }
        return jadwal;
    }
}
