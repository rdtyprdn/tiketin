package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class KasirJadwalController {

    private static final DateTimeFormatter FORMAT_JAM = DateTimeFormatter.ofPattern("HH.mm");

    private static final String GAYA_CARD =
        "-fx-background-color: #FFFFFF; -fx-background-radius: 12;"
        + " -fx-border-color: #E2E8F0; -fx-border-radius: 12;";

    private static final String GAYA_CARD_DIPILIH =
        "-fx-background-color: #E5EBF5; -fx-background-radius: 12;"
        + " -fx-border-color: #1A2C50; -fx-border-radius: 12;";

    @FXML
    private ImageView imgPosterFilm;

    @FXML
    private Label lblJudulFilm;

    @FXML
    private Label lblGenreDurasi;

    @FXML
    private Label lblSinopsis;

    @FXML
    private Label lblHargaTiket;

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    @FXML
    private Button btnTanggal1;

    @FXML
    private Button btnTanggal2;

    @FXML
    private Button btnTanggal3;

    @FXML
    private Button btnTanggal4;

    @FXML
    private Button btnTanggal5;

    @FXML
    private HBox containerJadwal;

    private final KasirTransaksiService service = new KasirTransaksiService();

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);

        Film film = KasirSession.getFilmTerpilih();
        if (film != null) {
            lblJudulFilm.setText(film.getJudul());
            lblGenreDurasi.setText(film.getGenre() + " · " + film.getDurasi() + " menit");
            lblSinopsis.setText(film.getSinopsis());
            imgPosterFilm.setImage(KasirUtil.poster(film));
        }

        tampilkanTanggal();
    }

    // Kembali ke halaman pemilihan film
    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirFilm");
    }

    // Isi 5 tombol tanggal mulai hari ini
    private void tampilkanTanggal() {
        LocalDate hariIni = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.forLanguageTag("id-ID"));

        Button[] tombolTanggal = semuaTombolTanggal();
        for (int i = 0; i < tombolTanggal.length; i++) {
            LocalDate tanggal = hariIni.plusDays(i);
            tombolTanggal[i].setText(tanggal.format(format));
            tombolTanggal[i].setUserData(tanggal);
        }

        // Memilih tanggal pertama secara otomatis
        pilihTanggal(new ActionEvent(btnTanggal1, btnTanggal1));
    }

    private Button[] semuaTombolTanggal() {
        return new Button[] { btnTanggal1, btnTanggal2, btnTanggal3, btnTanggal4, btnTanggal5 };
    }

    // Memilih tanggal tayang
    @FXML
    private void pilihTanggal(ActionEvent event) {
        Button tombol = (Button) event.getSource();
        LocalDate tanggalTerpilih = (LocalDate) tombol.getUserData();
        KasirSession.setTanggalTerpilih(tanggalTerpilih);

        for (Button btn : semuaTombolTanggal()) {
            btn.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #1A2C50;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: #CBD5E1;"
            );
        }
        tombol.setStyle(
            "-fx-background-color: #1A2C50;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #FBC436;"
        );

        // Reset jadwal ketika tanggal berubah
        KasirSession.setJadwalTerpilih(null);
        KasirSession.setJamTerpilih(null);
        KasirSession.setStudioTerpilih(null);

        tampilkanJadwal(tanggalTerpilih);
    }

    // Ambil jadwal dari database lalu buat card-nya
    private void tampilkanJadwal(LocalDate tanggal) {
        containerJadwal.getChildren().clear();
        lblHargaTiket.setText("-");

        Film film = KasirSession.getFilmTerpilih();
        if (film == null || tanggal == null) {
            return;
        }

        List<JadwalTayang> daftar;
        try {
            daftar = service.jadwalFilm(film.getIdFilm(), tanggal);
        } catch (IllegalArgumentException ex) {
            Peringatan.gagal(ex.getMessage());
            return;
        }

        if (daftar.isEmpty()) {
            Label kosong = new Label("Tidak ada jadwal tayang di tanggal ini.");
            kosong.setTextFill(Color.web("#64748b"));
            containerJadwal.getChildren().add(kosong);
            return;
        }

        // Harga ditampilkan dari jadwal pertama, berubah saat card dipilih
        lblHargaTiket.setText(KasirUtil.rupiah(daftar.get(0).getHargaTiket()));

        for (JadwalTayang jadwal : daftar) {
            containerJadwal.getChildren().add(buatCard(jadwal));
        }
    }

    // Card jadwal, gaya sama dengan desain Visvani
    private VBox buatCard(JadwalTayang jadwal) {
        String namaStudio = service.namaStudio(jadwal.getIdStudio());
        int sisa = service.sisaKursi(jadwal);

        ImageView ikon = new ImageView(new Image(getClass().getResourceAsStream("/com/tiketin/images/Studio.png")));
        ikon.setFitWidth(20);
        ikon.setFitHeight(20);
        ikon.setPreserveRatio(true);

        Label lblStudio = new Label(namaStudio, ikon);
        lblStudio.setGraphicTextGap(6);
        lblStudio.setTextFill(Color.web("#1a2c50"));
        lblStudio.setFont(Font.font("Inter 28pt SemiBold", 14));

        Label lblJam = new Label(jadwal.getWaktuMulai().format(FORMAT_JAM));
        lblJam.setTextFill(Color.web("#1a2c50"));
        lblJam.setFont(Font.font("Inter 28pt Bold", 22));

        Label lblSisa = new Label("Tersedia: " + sisa + " Kursi");
        lblSisa.setTextFill(Color.web("#1a2c50"));
        lblSisa.setStyle("-fx-background-color: #E5EBF5; -fx-background-radius: 20; -fx-padding: 3 14 3 14;");

        VBox card = new VBox(4, lblStudio, lblJam, lblSisa);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setCursor(Cursor.HAND);
        card.setStyle(GAYA_CARD);
        card.setUserData(jadwal);
        card.setOnMouseClicked(this::pilihJadwal);

        // Jadwal penuh tidak bisa dipilih
        if (sisa == 0) {
            card.setDisable(true);
            lblSisa.setText("Penuh");
        }
        return card;
    }

    // Memilih jam dan studio (jadwal disimpan di userData card)
    @FXML
    private void pilihJadwal(MouseEvent event) {
        VBox card = (VBox) event.getSource();
        if (!(card.getUserData() instanceof JadwalTayang jadwal)) {
            return;
        }

        KasirSession.setJadwalTerpilih(jadwal);
        KasirSession.setJamTerpilih(jadwal.getWaktuMulai().format(FORMAT_JAM));
        KasirSession.setStudioTerpilih(service.namaStudio(jadwal.getIdStudio()));
        lblHargaTiket.setText(KasirUtil.rupiah(jadwal.getHargaTiket()));

        for (Node node : containerJadwal.getChildren()) {
            if (node instanceof VBox) {
                node.setStyle(GAYA_CARD);
            }
        }
        card.setStyle(GAYA_CARD_DIPILIH);
    }

    @FXML
    private void lanjut() throws IOException {
        if (KasirSession.getJadwalTerpilih() == null) {
            Peringatan.gagal("Silakan pilih tanggal dan jadwal tayang terlebih dahulu.");
            return;
        }
        App.setRoot("fxml/kasir/KasirKursi");
    }
}
