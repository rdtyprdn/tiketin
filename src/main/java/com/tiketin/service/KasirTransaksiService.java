package com.tiketin.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.dao.DetailPemesananDAO;
import com.tiketin.dao.FilmDAO;
import com.tiketin.dao.JadwalTayangDAO;
import com.tiketin.dao.KursiDAO;
import com.tiketin.dao.PemesananDAO;
import com.tiketin.dao.StudioDAO;
import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusJadwal;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.KasirRiwayatItem;
import com.tiketin.model.KasirTiketItem;
import com.tiketin.model.Kursi;
import com.tiketin.model.Studio;
import com.tiketin.payment.PembayaranTunai;

// Semua kebutuhan data untuk halaman kasir
public class KasirTransaksiService {

    private final FilmDAO filmDAO = new FilmDAO();
    private final JadwalTayangDAO jadwalDAO = new JadwalTayangDAO();
    private final StudioDAO studioDAO = new StudioDAO();
    private final KursiDAO kursiDAO = new KursiDAO();
    private final DetailPemesananDAO detailDAO = new DetailPemesananDAO();
    private final PemesananDAO pemesananDAO = new PemesananDAO();
    private final PemesananService pemesananService = new PemesananService();

    public List<Film> daftarFilm() {
        try {
            return filmDAO.getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat film: " + e.getMessage());
        }
    }

    // Jadwal aktif untuk film tertentu di tanggal tertentu (yang belum lewat)
    public List<JadwalTayang> jadwalFilm(int idFilm, LocalDate tanggal) {
        try {
            List<JadwalTayang> hasil = new ArrayList<>();
            for (JadwalTayang j : jadwalDAO.getByFilm(idFilm)) {
                boolean aktif = j.getStatusJadwal() == null || j.getStatusJadwal() == StatusJadwal.AKTIF;
                if (aktif && j.getWaktuMulai() != null && j.getWaktuMulai().toLocalDate().equals(tanggal)) {
                    hasil.add(j);
                }
            }
            return hasil;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat jadwal: " + e.getMessage());
        }
    }

    public String namaStudio(int idStudio) {
        try {
            Studio studio = studioDAO.getById(idStudio);
            return studio == null ? "Studio " + idStudio : studio.getNamaStudio();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat studio: " + e.getMessage());
        }
    }

    // Jumlah kursi yang masih kosong untuk satu jadwal
    public int sisaKursi(JadwalTayang jadwal) {
        try {
            int total = kursiDAO.getByStudio(jadwal.getIdStudio()).size();
            int terisi = detailDAO.getKursiTerisi(jadwal.getIdJadwal()).size();
            return Math.max(0, total - terisi);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghitung kursi: " + e.getMessage());
        }
    }

    // Simpan transaksi tunai lewat PemesananService, hasilnya id pemesanan
    public int bayarTunai(JadwalTayang jadwal, List<Kursi> kursi, double uangDiterima, int idKasir) {
        List<Integer> idKursi = new ArrayList<>();
        for (Kursi k : kursi) {
            idKursi.add(k.getIdKursi());
        }
        try {
            return pemesananService.buatPemesanan(jadwal.getIdJadwal(), idKursi, KanalPemesanan.KASIR,
                    idKasir, new PembayaranTunai(uangDiterima));
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan transaksi: " + e.getMessage());
        }
    }

    public List<KasirRiwayatItem> riwayat(int idKasir) {
        try {
            return pemesananDAO.getRiwayatKasir(idKasir);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat riwayat: " + e.getMessage());
        }
    }

    public List<KasirTiketItem> tiket(int idPemesanan) {
        try {
            return pemesananDAO.getTiketPemesanan(idPemesanan);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat tiket: " + e.getMessage());
        }
    }
}
