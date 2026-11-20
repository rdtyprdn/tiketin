package com.tiketin.controller.kasir;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.tiketin.util.SceneManager;
import com.tiketin.model.Film;
import com.tiketin.util.KasirSession;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import com.tiketin.model.KasirRiwayatItem;
import com.tiketin.util.KasirRiwayatData;
import java.time.LocalDateTime;
import java.util.UUID;

public class KasirPembayaranController {
    private final int totalPembayaran = 100000;

    @FXML
    private ImageView imgPosterFilm;

    @FXML
    private Label lblJudulFilm;

    @FXML
    private Label lblGenreDurasi;

    @FXML
    private Label lblTanggal;

    @FXML
    private Label lblJamTayang;

    @FXML
    private Label lblStudio;

    @FXML
    private HBox containerKursi;

    @FXML
    private Label lblTotalPembayaran;

    @FXML
    private TextField txtNominalUang;

    @FXML
    private Label lblValidasiPembayaran;

    @FXML
    private Label lblKembalian;

    @FXML
    private Button btnKonfirmasiPembayaran;

    @FXML
    private void initialize() {
        lblStudio.setText(KasirSession.getStudioTerpilih());
        txtNominalUang.textProperty().addListener(
    (observable, oldValue, newValue) -> {
        hitungKembalian();
    }
    );
    hitungKembalian();

    Film film = KasirSession.getFilmTerpilih();

    if (film != null) {

        lblJudulFilm.setText(film.getJudul());

        lblGenreDurasi.setText(
            film.getGenre() + " · "
            + film.getDurasi() + " menit"
        );

        imgPosterFilm.setImage(new Image(
            getClass().getResourceAsStream(
                "/com/tiketin/images/" + film.getPoster()
            )
        ));
    }

    DateTimeFormatter format = DateTimeFormatter.ofPattern(
        "dd MMMM yyyy",
        Locale.forLanguageTag("id-ID")
    );

    if (KasirSession.getTanggalTerpilih() != null) {
        lblTanggal.setText(
            KasirSession.getTanggalTerpilih().format(format)
        );
    }

    lblJamTayang.setText(KasirSession.getJamTerpilih());
    lblStudio.setText(KasirSession.getStudioTerpilih());
    }

    private void hitungKembalian() {

    try {
        int nominal = Integer.parseInt(txtNominalUang.getText());

        if (nominal < totalPembayaran) {
            btnKonfirmasiPembayaran.setDisable(true);
            lblValidasiPembayaran.setText("Nominal pembayaran belum mencukupi.");
            lblValidasiPembayaran.setStyle("-fx-text-fill: #DC2626;");
            lblKembalian.setText("Rp 0");
        } else {
            int kembalian = nominal - totalPembayaran;
            btnKonfirmasiPembayaran.setDisable(false);  
            lblValidasiPembayaran.setText("Nominal pembayaran mencukupi.");
            lblValidasiPembayaran.setStyle("-fx-text-fill: #16A34A;");

            NumberFormat format = NumberFormat.getNumberInstance(
                Locale.forLanguageTag("id-ID")
            );

            lblKembalian.setText("Rp " + format.format(kembalian));
        }

    } catch (NumberFormatException e) {
        btnKonfirmasiPembayaran.setDisable(true);
        lblValidasiPembayaran.setText("Masukkan nominal uang yang valid.");
        lblValidasiPembayaran.setStyle("-fx-text-fill: #DC2626;");
        lblKembalian.setText("Rp 0");
    }
    }

   @FXML
   private void konfirmasiPembayaran() {

    if (btnKonfirmasiPembayaran.isDisabled()) {
        return;
    }

    String idTransaksi = "DEMO-" +
        UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    KasirRiwayatItem transaksi = new KasirRiwayatItem(
        idTransaksi,
        LocalDateTime.now(),
        KasirSession.getFilmTerpilih().getJudul(),
        2,
        totalPembayaran,
        "Lunas"
    );

    SceneManager.pindah(
    "kasir/KasirTiket",
    "Tiketin - Penerbitan Tiket"
    );

    KasirRiwayatData.tambahTransaksi(transaksi);
    }

    @FXML
    private void kembali() {
    SceneManager.pindah(
        "kasir/KasirRingkasan",
        "Tiketin - Ringkasan Pesanan"
    );
}
}
