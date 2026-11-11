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
        return film.stream().sorted(Comparator.comparingInt(Film::getIdFilm)).toList();
    }

    public List<Studio> studio() {
        return studio.stream().sorted(Comparator.comparingInt(Studio::getIdStudio)).toList();
    }

    public List<Kursi> kursi() {
        return kursi.stream().sorted(Comparator.comparingInt(Kursi::getIdKursi)).toList();
    }

    public List<JadwalTayang> jadwal() {
        return jadwal.stream().sorted(Comparator.comparing(JadwalTayang::getWaktuMulai).reversed()).toList();
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
        return film.stream().filter(item -> item.getIdFilm() == id).findFirst().orElse(null);
    }

    public Studio cariStudio(int id) {
        return studio.stream().filter(item -> item.getIdStudio() == id).findFirst().orElse(null);
    }

    public Kursi cariKursi(int id) {
        return kursi.stream().filter(item -> item.getIdKursi() == id).findFirst().orElse(null);
    }

    public JadwalTayang cariJadwal(int id) {
        return jadwal.stream().filter(item -> item.getIdJadwal() == id).findFirst().orElse(null);
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
        if (item.getIdFilm() == 0) {
            item.setIdFilm(++idFilm);
            film.add(item);
            return item;
        }
        ganti(film, item, Film::getIdFilm, item.getIdFilm(), "Film tidak ditemukan.");
        return item;
    }

    public void hapusFilm(int id) {
        if (!film.removeIf(item -> item.getIdFilm() == id)) {
            throw new IllegalArgumentException("Film tidak ditemukan.");
        }
    }

    public Studio simpanStudio(Studio item) {
        if (item.getIdStudio() == 0) {
            item.setIdStudio(++idStudio);
            studio.add(item);
            return item;
        }
        ganti(studio, item, Studio::getIdStudio, item.getIdStudio(), "Studio tidak ditemukan.");
        return item;
    }

    public void hapusStudio(int id) {
        if (!studio.removeIf(item -> item.getIdStudio() == id)) {
            throw new IllegalArgumentException("Studio tidak ditemukan.");
        }
    }

    public Kursi simpanKursi(Kursi item) {
        if (item.getIdKursi() == 0) {
            item.setIdKursi(++idKursi);
            kursi.add(item);
            return item;
        }
        ganti(kursi, item, Kursi::getIdKursi, item.getIdKursi(), "Kursi tidak ditemukan.");
        return item;
    }

    public void hapusKursi(int id) {
        if (!kursi.removeIf(item -> item.getIdKursi() == id)) {
            throw new IllegalArgumentException("Kursi tidak ditemukan.");
        }
    }

    public JadwalTayang simpanJadwal(JadwalTayang item) {
        if (item.getIdJadwal() == 0) {
            item.setIdJadwal(++idJadwal);
            jadwal.add(item);
            return item;
        }
        ganti(jadwal, item, JadwalTayang::getIdJadwal, item.getIdJadwal(), "Jadwal tidak ditemukan.");
        return item;
    }

    public void hapusJadwal(int id) {
        if (!jadwal.removeIf(item -> item.getIdJadwal() == id)) {
            throw new IllegalArgumentException("Jadwal tidak ditemukan.");
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
        Admin admin = new Admin(0, "admin", PasswordUtil.hash("admin123"), "Admin Bioskop", 0);
        admin.setIdPengguna(++idPengguna);
        admin.setIdAdmin(++idAdmin);
        pengguna.add(admin);
        ProfilService.terapkanTersimpan(admin);

        Kasir kasir = new Kasir(0, "kasir1", PasswordUtil.hash("kasir123"), "Kasir Loket", 0);
        kasir.setIdPengguna(++idPengguna);
        kasir.setIdKasir(++idKasir);
        pengguna.add(kasir);

        Film senja = simpanFilm(new Film(0, "Senja di Sanur", "Drama", 112,
                "Dua sahabat kembali ke Sanur dan menemukan surat yang belum pernah dikirim.", admin.getIdAdmin()));
        Film pulau = simpanFilm(new Film(0, "Pulau Sunyi", "Horor", 98,
                "Sebuah pulau wisata ditutup setelah mercusuarnya menyala sendiri setiap tengah malam.", admin.getIdAdmin()));
        Film jejak = simpanFilm(new Film(0, "Jejak Emas", "Petualangan", 126,
                "Peta tua membawa tiga mahasiswa menyusuri gua di utara Bali.", admin.getIdAdmin()));

        Studio studio1 = simpanStudio(new Studio(0, "Studio 1", admin.getIdAdmin()));
        Studio studio2 = simpanStudio(new Studio(0, "Studio 2", admin.getIdAdmin()));

        Kursi a1 = simpanKursi(new Kursi(0, studio1.getIdStudio(), "A1", "VIP", admin.getIdAdmin()));
        Kursi a2 = simpanKursi(new Kursi(0, studio1.getIdStudio(), "A2", "Reguler", admin.getIdAdmin()));
        simpanKursi(new Kursi(0, studio1.getIdStudio(), "A3", "Reguler", admin.getIdAdmin()));
        Kursi b1 = simpanKursi(new Kursi(0, studio1.getIdStudio(), "B1", "Reguler", admin.getIdAdmin()));
        simpanKursi(new Kursi(0, studio1.getIdStudio(), "B2", "Reguler", admin.getIdAdmin()));
        Kursi c1 = simpanKursi(new Kursi(0, studio2.getIdStudio(), "C1", "Reguler", admin.getIdAdmin()));
        Kursi c2 = simpanKursi(new Kursi(0, studio2.getIdStudio(), "C2", "Reguler", admin.getIdAdmin()));

        LocalDate hariIni = LocalDate.now();
        JadwalTayang tayangBesok = simpanJadwal(new JadwalTayang(0, senja.getIdFilm(), studio1.getIdStudio(),
                LocalDateTime.of(hariIni.plusDays(1), LocalTime.of(19, 0)), 50000, StatusJadwal.AKTIF, admin.getIdAdmin()));
        JadwalTayang pulauBesok = simpanJadwal(new JadwalTayang(0, pulau.getIdFilm(), studio2.getIdStudio(),
                LocalDateTime.of(hariIni.plusDays(1), LocalTime.of(21, 0)), 45000, StatusJadwal.AKTIF, admin.getIdAdmin()));
        simpanJadwal(new JadwalTayang(0, jejak.getIdFilm(), studio1.getIdStudio(),
                LocalDateTime.of(hariIni, LocalTime.of(16, 0)), 60000, StatusJadwal.DIBATALKAN, admin.getIdAdmin()));
        JadwalTayang senjaLalu = simpanJadwal(new JadwalTayang(0, senja.getIdFilm(), studio1.getIdStudio(),
                LocalDateTime.of(hariIni.minusDays(2), LocalTime.of(19, 0)), 50000, StatusJadwal.AKTIF, admin.getIdAdmin()));
        JadwalTayang pulauLalu = simpanJadwal(new JadwalTayang(0, pulau.getIdFilm(), studio2.getIdStudio(),
                LocalDateTime.of(hariIni.minusDays(3), LocalTime.of(21, 0)), 45000, StatusJadwal.AKTIF, admin.getIdAdmin()));

        pesan(senjaLalu, kasir.getIdKasir(), KanalPemesanan.LOKET, StatusPemesanan.BERHASIL,
                hariIni.minusDays(2).atTime(19, 20), 50000, a2);
        pesan(pulauLalu, null, KanalPemesanan.KIOSK, StatusPemesanan.BERHASIL,
                hariIni.minusDays(3).atTime(21, 10), 90000, c1, c2);
        pesan(tayangBesok, null, KanalPemesanan.KIOSK, StatusPemesanan.MENUNGGU_PEMBAYARAN,
                LocalDateTime.now().minusHours(1), 50000, b1);
        pesan(tayangBesok, null, KanalPemesanan.KIOSK, StatusPemesanan.DIBATALKAN,
                LocalDateTime.now().minusHours(3), 50000, a1);
        pesan(pulauBesok, kasir.getIdKasir(), KanalPemesanan.LOKET, StatusPemesanan.BERHASIL,
                LocalDateTime.now().minusMinutes(30), 45000, c1);
    }

    private void pesan(JadwalTayang jadwalTayang, Integer idKasirPesanan, KanalPemesanan kanal,
                       StatusPemesanan status, LocalDateTime waktu, int total, Kursi... kursiDipilih) {
        Pemesanan pesanan = new Pemesanan(++idPemesanan, jadwalTayang.getIdJadwal(), idKasirPesanan,
                ++idPelanggan, waktu, kanal, status, total);
        pemesanan.add(pesanan);
        for (Kursi item : kursiDipilih) {
            detail.add(new DetailPemesanan(++idDetail, pesanan.getIdPemesanan(),
                    jadwalTayang.getIdJadwal(), item.getIdKursi()));
        }
    }
}
