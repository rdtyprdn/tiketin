package com.tiketin.service;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import com.tiketin.dao.DetailPemesananDAO;
import com.tiketin.dao.FilmDAO;
import com.tiketin.dao.JadwalTayangDAO;
import com.tiketin.dao.KursiDAO;
import com.tiketin.dao.PemesananDAO;
import com.tiketin.dao.PenggunaDAO;
import com.tiketin.dao.StudioDAO;
import com.tiketin.model.DetailPemesanan;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kasir;
import com.tiketin.model.Kursi;
import com.tiketin.model.Pemesanan;
import com.tiketin.model.Pengguna;
import com.tiketin.model.Studio;

/**
 * Pintu akses data bersama untuk service admin (Auth, Film, Studio, Kursi, Jadwal,
 * Kasir, Transaksi, Laporan, Profil).
 *
 * Dulu kelas ini menyimpan data contoh di memori. Sekarang semua method langsung
 * membaca/menulis MySQL lewat DAO, jadi namanya saja yang masih "Sementara".
 * Error SQL diubah jadi IllegalArgumentException supaya pesan bisa langsung
 * ditampilkan controller (sama seperti sebelumnya).
 */
public final class PenyimpananSementara {
    private static final PenyimpananSementara AKTIF = new PenyimpananSementara();

    private PenyimpananSementara() {
    }

    public static PenyimpananSementara get() {
        return AKTIF;
    }

    public List<Pengguna> pengguna() {
        try {
            return new PenggunaDAO().getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat pengguna: " + e.getMessage());
        }
    }

    public List<Film> film() {
        try {
            return new FilmDAO().getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat film: " + e.getMessage());
        }
    }

    public List<Studio> studio() {
        try {
            return new StudioDAO().getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat studio: " + e.getMessage());
        }
    }

    public List<Kursi> kursi() {
        try {
            return new KursiDAO().getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat kursi: " + e.getMessage());
        }
    }

    public List<JadwalTayang> jadwal() {
        try {
            return new JadwalTayangDAO().getAll().stream()
                    .sorted(Comparator.comparing(JadwalTayang::getWaktuMulai).reversed()).toList();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat jadwal: " + e.getMessage());
        }
    }

    public List<Pemesanan> pemesanan() {
        try {
            return new PemesananDAO().getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat pemesanan: " + e.getMessage());
        }
    }

    public List<DetailPemesanan> detail() {
        try {
            return new DetailPemesananDAO().getAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat detail pemesanan: " + e.getMessage());
        }
    }

    public Pengguna cariUsername(String username) {
        try {
            return new PenggunaDAO().getByUsername(username);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari pengguna: " + e.getMessage());
        }
    }

    public Film cariFilm(int id) {
        try {
            return new FilmDAO().getById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari film: " + e.getMessage());
        }
    }

    public Studio cariStudio(int id) {
        try {
            return new StudioDAO().getById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari studio: " + e.getMessage());
        }
    }

    public Kursi cariKursi(int id) {
        try {
            return new KursiDAO().getById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari kursi: " + e.getMessage());
        }
    }

    public JadwalTayang cariJadwal(int id) {
        try {
            return new JadwalTayangDAO().getById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari jadwal: " + e.getMessage());
        }
    }

    public Kasir cariKasir(int idKasir) {
        for (Pengguna orang : pengguna()) {
            if (orang instanceof Kasir kasir && kasir.getIdKasir() == idKasir) {
                return kasir;
            }
        }
        return null;
    }

    public Film simpanFilm(Film item) {
        try {
            if (item.getIdFilm() == 0) {
                new FilmDAO().tambah(item);
            } else {
                new FilmDAO().ubah(item);
            }
            return item;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan film: " + e.getMessage());
        }
    }

    public void hapusFilm(int id) {
        try {
            new FilmDAO().hapus(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus film: " + e.getMessage());
        }
    }

    public Studio simpanStudio(Studio item) {
        try {
            if (item.getIdStudio() == 0) {
                new StudioDAO().tambah(item);
            } else {
                new StudioDAO().ubah(item);
            }
            return item;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan studio: " + e.getMessage());
        }
    }

    public void hapusStudio(int id) {
        try {
            new StudioDAO().hapus(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus studio: " + e.getMessage());
        }
    }

    public Kursi simpanKursi(Kursi item) {
        try {
            if (item.getIdKursi() == 0) {
                new KursiDAO().tambah(item);
            } else {
                new KursiDAO().ubah(item);
            }
            return item;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan kursi: " + e.getMessage());
        }
    }

    public void hapusKursi(int id) {
        try {
            new KursiDAO().hapus(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus kursi: " + e.getMessage());
        }
    }

    public JadwalTayang simpanJadwal(JadwalTayang item) {
        try {
            if (item.getIdJadwal() == 0) {
                new JadwalTayangDAO().tambah(item);
            } else {
                new JadwalTayangDAO().ubah(item);
            }
            return item;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan jadwal: " + e.getMessage());
        }
    }

    public void hapusJadwal(int id) {
        try {
            new JadwalTayangDAO().hapus(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus jadwal: " + e.getMessage());
        }
    }

    public Kasir simpanKasir(Kasir item) {
        try {
            if (item.getIdPengguna() == 0) {
                new PenggunaDAO().tambahKasir(item);
            } else {
                new PenggunaDAO().ubahKasir(item);
            }
            return item;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan kasir: " + e.getMessage());
        }
    }

    public void hapusKasir(int idKasirDihapus) {
        try {
            new PenggunaDAO().hapusKasir(idKasirDihapus);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus kasir: " + e.getMessage());
        }
    }

    public boolean usernameDipakai(String username, int kecualiIdPengguna) {
        return pengguna().stream().anyMatch(item ->
                item.getUsername().equalsIgnoreCase(username) && item.getIdPengguna() != kecualiIdPengguna);
    }
}
