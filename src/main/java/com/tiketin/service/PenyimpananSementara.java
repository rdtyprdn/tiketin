package com.tiketin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusJadwal;
import com.tiketin.enums.StatusPemesanan;
import com.tiketin.model.Admin;
import com.tiketin.model.DetailPemesanan;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kasir;
import com.tiketin.model.Kursi;
import com.tiketin.model.Pemesanan;
import com.tiketin.model.Pengguna;
import com.tiketin.model.Studio;
import com.tiketin.util.PasswordUtil;

/**
 * Data contoh di memori supaya halaman admin bisa dicoba sebelum DAO MySQL dipasang.
 * Controller tidak membaca kelas ini. Service yang mengganti isi method ke DAO.
 */
public final class PenyimpananSementara {
    private static final PenyimpananSementara AKTIF = new PenyimpananSementara();

    private final List<Pengguna> pengguna = new ArrayList<>();
    private final List<Film> film = new ArrayList<>();
    private final List<Studio> studio = new ArrayList<>();
    private final List<Kursi> kursi = new ArrayList<>();
    private final List<JadwalTayang> jadwal = new ArrayList<>();
    private final List<Pemesanan> pemesanan = new ArrayList<>();
    private final List<DetailPemesanan> detail = new ArrayList<>();

    private int idPengguna;
    private int idAdmin;
    private int idKasir;
    private int idFilm;
    private int idStudio;
    private int idKursi;
    private int idJadwal;
    private int idPemesanan;
    private int idDetail;
    private int idPelanggan;

    private PenyimpananSementara() {
        isiContoh();
    }

    public static PenyimpananSementara get() {
        return AKTIF;
    }

    public List<Pengguna> pengguna() {
        return List.copyOf(pengguna);
    }

    public List<Film> film() {
        try {
            return new com.tiketin.dao.FilmDAO().getAll();
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal memuat film: " + e.getMessage());
        }
    }

    public List<Studio> studio() {
        try {
            return new com.tiketin.dao.StudioDAO().getAll();
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal memuat studio: " + e.getMessage());
        }
    }


     public List<Kursi> kursi() {
        try {
            return new com.tiketin.dao.KursiDAO().getAll();
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal memuat kursi: " + e.getMessage());
        }
    }

    public List<JadwalTayang> jadwal() {
        try {
            return new com.tiketin.dao.JadwalTayangDAO().getAll().stream()
                    .sorted(Comparator.comparing(JadwalTayang::getWaktuMulai).reversed()).toList();
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal memuat jadwal: " + e.getMessage());
        }
    }

    public List<Pemesanan> pemesanan() {
        return pemesanan.stream().sorted(Comparator.comparing(Pemesanan::getWaktuPemesanan).reversed()).toList();
    }

    public List<DetailPemesanan> detail() {
        return List.copyOf(detail);
    }

    public Pengguna cariUsername(String username) {
        for (Pengguna orang : pengguna) {
            if (orang.getUsername().equals(username)) {
                return orang;
            }
        }
        return null;
    }

    public Film cariFilm(int id) {
        try {
            return new com.tiketin.dao.FilmDAO().getById(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal mencari film: " + e.getMessage());
        }
    }

    public Studio cariStudio(int id) {
        try {
            return new com.tiketin.dao.StudioDAO().getById(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal mencari studio: " + e.getMessage());
        }
    }

    public Kursi cariKursi(int id) {
        try {
            return new com.tiketin.dao.KursiDAO().getById(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal mencari kursi: " + e.getMessage());
        }
    }

    public JadwalTayang cariJadwal(int id) {
        try {
            return new com.tiketin.dao.JadwalTayangDAO().getById(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal mencari jadwal: " + e.getMessage());
        }
    }

    public Kasir cariKasir(int idKasir) {
        for (Pengguna orang : pengguna) {
            if (orang instanceof Kasir kasir && kasir.getIdKasir() == idKasir) {
                return kasir;
            }
        }
        return null;
    }

    public Film simpanFilm(Film item) {
        try {
            if (item.getIdFilm() == 0) {
                new com.tiketin.dao.FilmDAO().tambah(item);
            } else {
                new com.tiketin.dao.FilmDAO().ubah(item);
            }
            return item;
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan film: " + e.getMessage());
        }
    }

      public void hapusFilm(int id) {
        try {
            new com.tiketin.dao.FilmDAO().hapus(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus film: " + e.getMessage());
        }
    }


    public Studio simpanStudio(Studio item) {
        try {
            if (item.getIdStudio() == 0) {
                new com.tiketin.dao.StudioDAO().tambah(item);
            } else {
                new com.tiketin.dao.StudioDAO().ubah(item);
            }
            return item;
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan studio: " + e.getMessage());
        }
    }


    public void hapusStudio(int id) {
        try {
            new com.tiketin.dao.StudioDAO().hapus(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus studio: " + e.getMessage());
        }
    }

    public Kursi simpanKursi(Kursi item) {
        try {
            if (item.getIdKursi() == 0) {
                new com.tiketin.dao.KursiDAO().tambah(item);
            } else {
                new com.tiketin.dao.KursiDAO().ubah(item);
            }
            return item;
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan kursi: " + e.getMessage());
        }
    }

    public void hapusKursi(int id) {
        try {
            new com.tiketin.dao.KursiDAO().hapus(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus kursi: " + e.getMessage());
        }
    }

    public JadwalTayang simpanJadwal(JadwalTayang item) {
        try {
            if (item.getIdJadwal() == 0) {
                new com.tiketin.dao.JadwalTayangDAO().tambah(item);
            } else {
                new com.tiketin.dao.JadwalTayangDAO().ubah(item);
            }
            return item;
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan jadwal: " + e.getMessage());
        }
    }

    public void hapusJadwal(int id) {
        try {
            new com.tiketin.dao.JadwalTayangDAO().hapus(id);
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus jadwal: " + e.getMessage());
        }
    }

    public Kasir simpanKasir(Kasir item) {
        if (item.getIdPengguna() == 0) {
            item.setIdPengguna(++idPengguna);
            item.setIdKasir(++idKasir);
            pengguna.add(item);
            return item;
        }
        ganti(pengguna, item, Pengguna::getIdPengguna, item.getIdPengguna(), "Kasir tidak ditemukan.");
        return item;
    }

    public void hapusKasir(int idKasirDihapus) {
        if (!pengguna.removeIf(item -> item instanceof Kasir kasir && kasir.getIdKasir() == idKasirDihapus)) {
            throw new IllegalArgumentException("Kasir tidak ditemukan.");
        }
    }

    public boolean usernameDipakai(String username, int kecualiIdPengguna) {
        return pengguna.stream().anyMatch(item ->
                item.getUsername().equalsIgnoreCase(username) && item.getIdPengguna() != kecualiIdPengguna);
    }

    private <T> void ganti(List<T> daftar, T baru, java.util.function.ToIntFunction<T> id, int nilaiId, String pesan) {
        for (int i = 0; i < daftar.size(); i++) {
            if (id.applyAsInt(daftar.get(i)) == nilaiId) {
                daftar.set(i, baru);
                return;
            }
        }
        throw new IllegalArgumentException(pesan);
    }

    private void isiContoh() {
        Admin admin = new Admin(0, 0, "admin", PasswordUtil.hash("admin123"), "Admin Bioskop");
        admin.setIdPengguna(++idPengguna);
        admin.setIdAdmin(++idAdmin);
        pengguna.add(admin);
        ProfilService.terapkanTersimpan(admin);

        Kasir kasir = new Kasir(0, 0, "kasir1", PasswordUtil.hash("kasir123"), "Kasir Loket");
        kasir.setIdPengguna(++idPengguna);
        kasir.setIdKasir(++idKasir);
        pengguna.add(kasir);

        // Film senja = simpanFilm(new Film(0, "Senja di Sanur", "Drama", 112,
          //      "Dua sahabat kembali ke Sanur dan menemukan surat yang belum pernah dikirim.", admin.getIdAdmin()));
        // Film pulau = simpanFilm(new Film(0, "Pulau Sunyi", "Horor", 98,
          //      "Sebuah pulau wisata ditutup setelah mercusuarnya menyala sendiri setiap tengah malam.", admin.getIdAdmin()));
        // Film jejak = simpanFilm(new Film(0, "Jejak Emas", "Petualangan", 126,
          //      "Peta tua membawa tiga mahasiswa menyusuri gua di utara Bali.", admin.getIdAdmin()));

        // Studio studio1 = simpanStudio(new Studio(0, "Studio 1", admin.getIdAdmin()));
        // Studio studio2 = simpanStudio(new Studio(0, "Studio 2", admin.getIdAdmin()));

        // Kursi a1 = simpanKursi(new Kursi(0, 1, "A1", "VIP", admin.getIdAdmin()));
        // Kursi a2 = simpanKursi(new Kursi(0, 1, "A2", "Reguler", admin.getIdAdmin()));
        // simpanKursi(new Kursi(0, 1, "A3", "Reguler", admin.getIdAdmin()));
        // Kursi b1 = simpanKursi(new Kursi(0, 1, "B1", "Reguler", admin.getIdAdmin()));
        // simpanKursi(new Kursi(0, 1, "B2", "Reguler", admin.getIdAdmin()));
        // Kursi c1 = simpanKursi(new Kursi(0, 2, "C1", "Reguler", admin.getIdAdmin()));
        // Kursi c2 = simpanKursi(new Kursi(0, 2, "C2", "Reguler", admin.getIdAdmin()));

        LocalDate hariIni = LocalDate.now();
       // JadwalTayang tayangBesok =simpanJadwal(new JadwalTayang(0, 1, 1,
         //       LocalDateTime.of(hariIni.plusDays(1), LocalTime.of(19, 0)), 50000, StatusJadwal.AKTIF, admin.getIdAdmin()));
        // JadwalTayang pulauBesok = simpanJadwal(new JadwalTayang(0, 2, 2,
         //       LocalDateTime.of(hariIni.plusDays(1), LocalTime.of(21, 0)), 45000, StatusJadwal.AKTIF, admin.getIdAdmin()));
        // simpanJadwal(new JadwalTayang(0, 3, 1,
          //      LocalDateTime.of(hariIni, LocalTime.of(16, 0)), 60000, StatusJadwal.DIBATALKAN, admin.getIdAdmin()));
        // JadwalTayang senjaLalu = simpanJadwal(new JadwalTayang(0, 1, 1,
          //      LocalDateTime.of(hariIni.minusDays(2), LocalTime.of(19, 0)), 50000, StatusJadwal.AKTIF, admin.getIdAdmin()));
        // JadwalTayang pulauLalu = simpanJadwal(new JadwalTayang(0, 2, 2,
          //      LocalDateTime.of(hariIni.minusDays(3), LocalTime.of(21, 0)), 45000, StatusJadwal.AKTIF, admin.getIdAdmin()));

       // pesan(senjaLalu, kasir.getIdKasir(), KanalPemesanan.KASIR, StatusPemesanan.LUNAS,
           //     hariIni.minusDays(2).atTime(19, 20), 50000, a2);
       // pesan(pulauLalu, null, KanalPemesanan.KIOSK, StatusPemesanan.LUNAS,
             //   hariIni.minusDays(3).atTime(21, 10), 90000, c1, c2);
       // pesan(tayangBesok, null, KanalPemesanan.KIOSK, StatusPemesanan.MENUNGGU_PEMBAYARAN,
               // LocalDateTime.now().minusHours(1), 50000, b1);
       // pesan(tayangBesok, null, KanalPemesanan.KIOSK, StatusPemesanan.DIBATALKAN,
               // LocalDateTime.now().minusHours(3), 50000, a1);
       // pesan(pulauBesok, kasir.getIdKasir(), KanalPemesanan.KASIR, StatusPemesanan.LUNAS,
                // LocalDateTime.now().minusMinutes(30), 45000, c1);
    }

    private void pesan(JadwalTayang jadwalTayang, Integer idKasirPesanan, KanalPemesanan kanal,
                       StatusPemesanan status, LocalDateTime waktu, int total, Kursi... kursiDipilih) {
        Pemesanan pesanan = new Pemesanan(++idPemesanan, jadwalTayang.getIdJadwal(), idKasirPesanan,
                ++idPelanggan, waktu, kanal, status, total);
        pemesanan.add(pesanan);
        for (Kursi item : kursiDipilih) {
            detail.add(new DetailPemesanan(++idDetail, pesanan.getIdPemesanan(),
                    jadwalTayang.getIdJadwal(), item.getIdKursi(), jadwalTayang.getHargaTiket()));
        }
    }
}
