package com.tiketin.controller.customer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.service.FilmService;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.KioskSession;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class KioskScheduleSelectionController {

    // INFORMASI FILM
    @FXML private ImageView imgPosterFilm;
    @FXML private Label lblJudulFilm;
    @FXML private Label lblGenre;
    @FXML private Label lblDurasi;
    @FXML private Label lblRatingUsia;
    @FXML private Label lblRating;
    @FXML private Label lblHargaTiket;

    // PILIHAN TANGGAL (5 kartu dari desain Alia, diisi hari ini s.d. 4 hari ke depan)
    @FXML private VBox cardTanggal15;
    @FXML private VBox cardTanggal16;
    @FXML private VBox cardTanggal17;
    @FXML private VBox cardTanggal18;
    @FXML private VBox cardTanggal19;

    // PILIHAN JADWAL (5 kartu dari desain Alia, diisi jadwal dari database)
    @FXML private VBox cardJadwal1030;
    @FXML private VBox cardJadwal1315;
    @FXML private VBox cardJadwal1600;
    @FXML private VBox cardJadwal1845;
    @FXML private VBox cardJadwal2100;

    // TOMBOL
    @FXML private Button btnLanjut;

    private static final Locale ID = Locale.forLanguageTag("id-ID");
    private static final DateTimeFormatter FORMAT_TANGGAL = DateTimeFormatter.ofPattern("EEE, dd MMM", ID);
    private static final DateTimeFormatter FORMAT_JAM = DateTimeFormatter.ofPattern("HH.mm");

    // STYLE TANGGAL
    private static final String BORDER_NORMAL_TANGGAL =
        "-fx-background-color: #FFFFFF; " +
        "-fx-background-radius: 8; " +
        "-fx-border-color: #E1E5EF; " +
        "-fx-border-radius: 8; " +
        "-fx-border-width: 2; " +
        "-fx-padding: 7;";

    private static final String BORDER_SELECTED_TANGGAL =
        "-fx-background-color: #FFFFFF; " +
        "-fx-background-radius: 8; " +
        "-fx-border-color: #FFC43D; " +
        "-fx-border-radius: 8; " +
        "-fx-border-width: 2; " +
        "-fx-padding: 7;";

    // STYLE JADWAL
    private static final String BORDER_NORMAL_JADWAL =
        "-fx-background-color: #FFFFFF; " +
        "-fx-background-radius: 10; " +
        "-fx-border-color: #DCE1EF; " +
        "-fx-border-radius: 10; " +
        "-fx-border-width: 1; " +
        "-fx-padding: 10 5 10 5;";

    private static final String BORDER_SELECTED_JADWAL =
        "-fx-background-color: #FFFFFF; " +
        "-fx-background-radius: 10; " +
        "-fx-border-color: #FFC43D; " +
        "-fx-border-radius: 10; " +
        "-fx-border-width: 2; " +
        "-fx-padding: 10 5 10 5;";

    private static final String JADWAL_PENUH =
        BORDER_NORMAL_JADWAL + " -fx-opacity: 0.5;";

    private final KasirTransaksiService service = new KasirTransaksiService();

    private VBox[] kartuTanggal;
    private VBox[] kartuJadwal;
    private final LocalDate[] tanggalKartu = new LocalDate[5];
    private final List<JadwalTayang> jadwalTampil = new ArrayList<>();

    // DATA PILIHAN
    private Film film;
    private LocalDate tanggalDipilih;
    private JadwalTayang jadwalDipilih;
    private String studioDipilih;

    @FXML
    private void initialize() {
        kartuTanggal = new VBox[] { cardTanggal15, cardTanggal16, cardTanggal17, cardTanggal18, cardTanggal19 };
        kartuJadwal = new VBox[] { cardJadwal1030, cardJadwal1315, cardJadwal1600, cardJadwal1845, cardJadwal2100 };
        btnLanjut.setDisable(true);

        film = KioskSession.getFilmTerpilih();
        if (film == null) {
            return; // dibuka tanpa memilih film
        }
        tampilkanFilm();
        aturPilihanTanggal();

        // Pulihkan pilihan sebelumnya (misal kembali dari halaman kursi), kalau tidak pilih hari ini
        LocalDate awal = KioskSession.getTanggalTerpilih();
        int indeks = 0;
        for (int i = 0; i < tanggalKartu.length; i++) {
            if (tanggalKartu[i].equals(awal)) {
                indeks = i;
            }
        }
        pilihTanggal(indeks);

        JadwalTayang sebelumnya = KioskSession.getJadwalTerpilih();
        if (sebelumnya != null) {
            for (int i = 0; i < jadwalTampil.size(); i++) {
                if (jadwalTampil.get(i).getIdJadwal() == sebelumnya.getIdJadwal()) {
                    pilihJadwal(i);
                }
            }
        }
    }

    private void tampilkanFilm() {
        lblJudulFilm.setText(film.getJudul());
        lblGenre.setText(film.getGenre() == null || film.getGenre().isBlank() ? "-" : film.getGenre());
        lblDurasi.setText(film.getDurasi() + " min");
        lblRating.setText(FilmService.formatRating(film.getRating()));
        lblRatingUsia.setText(film.getRatingUsia() == null || film.getRatingUsia().isBlank() ? "-" : film.getRatingUsia());
        lblHargaTiket.setText("-");
        imgPosterFilm.setImage(KasirUtil.poster(film));
    }

    // MENGATUR PILIHAN TANGGAL
    private void aturPilihanTanggal() {
        LocalDate hariIni = LocalDate.now();
        for (int i = 0; i < kartuTanggal.length; i++) {
            final int indeks = i;
            tanggalKartu[i] = hariIni.plusDays(i);
            List<Label> label = cariLabel(kartuTanggal[i]);
            if (!label.isEmpty()) {
                label.get(0).setText(tanggalKartu[i].format(FORMAT_TANGGAL));
            }
            kartuTanggal[i].setOnMouseClicked(event -> pilihTanggal(indeks));
        }
    }

    private void pilihTanggal(int indeks) {
        tanggalDipilih = tanggalKartu[indeks];
        for (VBox item : kartuTanggal) {
            item.setStyle(BORDER_NORMAL_TANGGAL);
        }
        kartuTanggal[indeks].setStyle(BORDER_SELECTED_TANGGAL);

        jadwalDipilih = null;
        studioDipilih = null;
        muatJadwal();
        periksaPilihan();
    }

    // MENGISI KARTU JADWAL DARI DATABASE
    private void muatJadwal() {
        jadwalTampil.clear();
        try {
            jadwalTampil.addAll(service.jadwalFilm(film.getIdFilm(), tanggalDipilih));
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        }
        if (jadwalTampil.size() > kartuJadwal.length) {
            // desain hanya punya 5 kartu jadwal per hari
            jadwalTampil.subList(kartuJadwal.length, jadwalTampil.size()).clear();
        }

        for (int i = 0; i < kartuJadwal.length; i++) {
            VBox kartu = kartuJadwal[i];
            kartu.setOnMouseClicked(null);
            kartu.setStyle(BORDER_NORMAL_JADWAL);
            boolean ada = i < jadwalTampil.size();
            kartu.setVisible(ada);
            kartu.setManaged(ada);
            if (ada) {
                isiKartuJadwal(i, jadwalTampil.get(i));
            }
        }

        if (jadwalTampil.isEmpty()) {
            // pakai kartu pertama untuk pesan "tidak ada jadwal"
            VBox kartu = kartuJadwal[0];
            kartu.setVisible(true);
            kartu.setManaged(true);
            isiLabelJadwal(kartu, "-", "Tidak ada jadwal", "Pilih tanggal lain");
            lblHargaTiket.setText("-");
        } else {
            lblHargaTiket.setText(KasirUtil.rupiah(jadwalTampil.get(0).getHargaTiket()));
        }
    }

    private void isiKartuJadwal(int indeks, JadwalTayang jadwal) {
        VBox kartu = kartuJadwal[indeks];
        String studio = service.namaStudio(jadwal.getIdStudio());
        int sisa = service.sisaKursi(jadwal);
        isiLabelJadwal(kartu, studio, jadwal.getWaktuMulai().format(FORMAT_JAM),
            sisa > 0 ? "Tersedia: " + sisa + " Kursi" : "Penuh");

        if (sisa > 0) {
            kartu.setOnMouseClicked(event -> pilihJadwal(indeks));
        } else {
            kartu.setStyle(JADWAL_PENUH);
        }
    }

    // urutan label di kartu Alia: nama studio, jam, sisa kursi
    private void isiLabelJadwal(VBox kartu, String studio, String jam, String sisa) {
        List<Label> label = cariLabel(kartu);
        if (label.size() >= 3) {
            label.get(0).setText(studio);
            label.get(1).setText(jam);
            label.get(2).setText(sisa);
        }
    }

    private void pilihJadwal(int indeks) {
        jadwalDipilih = jadwalTampil.get(indeks);
        studioDipilih = service.namaStudio(jadwalDipilih.getIdStudio());

        for (int i = 0; i < jadwalTampil.size(); i++) {
            if (!JADWAL_PENUH.equals(kartuJadwal[i].getStyle())) {
                kartuJadwal[i].setStyle(BORDER_NORMAL_JADWAL);
            }
        }
        kartuJadwal[indeks].setStyle(BORDER_SELECTED_JADWAL);
        lblHargaTiket.setText(KasirUtil.rupiah(jadwalDipilih.getHargaTiket()));
        periksaPilihan();
    }

    // Ambil semua Label di dalam kartu, urut dari atas ke bawah
    private List<Label> cariLabel(Node node) {
        List<Label> hasil = new ArrayList<>();
        if (node instanceof Label label) {
            hasil.add(label);
        } else if (node instanceof Parent parent) {
            for (Node anak : parent.getChildrenUnmodifiable()) {
                hasil.addAll(cariLabel(anak));
            }
        }
        return hasil;
    }

    // MEMERIKSA PILIHAN
    private void periksaPilihan() {
        btnLanjut.setDisable(tanggalDipilih == null || jadwalDipilih == null);
    }

    // KEMBALI KE HALAMAN DETAIL FILM
    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/movie-detail");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LANJUT KE PILIH KURSI
    @FXML
    private void handleLanjut() {
        if (tanggalDipilih == null || jadwalDipilih == null) {
            return;
        }
        KioskSession.setTanggalTerpilih(tanggalDipilih);
        KioskSession.setJadwalTerpilih(jadwalDipilih, studioDipilih);
        try {
            App.setRoot("fxml/customer/kiosk-seat-selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
