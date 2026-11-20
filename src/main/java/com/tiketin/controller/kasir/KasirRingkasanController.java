package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kursi;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class KasirRingkasanController {

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
    private Label lblHargaTiket;

    @FXML
    private Label lblJumlahTiket;

    @FXML
    private Label lblTotalPembayaran;

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);

        Film film = KasirSession.getFilmTerpilih();
        if (film != null) {
            lblJudulFilm.setText(film.getJudul());
            lblGenreDurasi.setText(film.getGenre() + " · " + film.getDurasi() + " menit");
            imgPosterFilm.setImage(KasirUtil.poster(film));
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
        if (KasirSession.getTanggalTerpilih() != null) {
            lblTanggal.setText(KasirSession.getTanggalTerpilih().format(format));
        }
        lblJamTayang.setText(KasirSession.getJamTerpilih());
        lblStudio.setText(KasirSession.getStudioTerpilih());

        // Kursi terpilih dan rincian harga
        List<Kursi> kursi = KasirSession.getKursiTerpilih();
        tampilkanKursi(containerKursi, kursi);

        JadwalTayang jadwal = KasirSession.getJadwalTerpilih();
        double harga = jadwal == null ? 0 : jadwal.getHargaTiket();
        lblHargaTiket.setText(KasirUtil.rupiah(harga));
        lblJumlahTiket.setText(kursi.size() + " Tiket");
        lblTotalPembayaran.setText(KasirUtil.rupiah(KasirSession.getTotalBayar()));
    }

    // Kotak nomor kursi, gaya sama dengan desain di FXML
    static void tampilkanKursi(HBox wadah, List<Kursi> kursi) {
        wadah.getChildren().clear();
        for (Kursi k : kursi) {
            Label kotak = new Label(k.getNomorKursi());
            kotak.setAlignment(Pos.CENTER);
            kotak.setPrefSize(60, 40);
            kotak.setTextFill(Color.WHITE);
            kotak.setFont(Font.font("Inter 28pt SemiBold", 16));
            kotak.setStyle("-fx-background-color: #1A2C50; -fx-background-radius: 8; -fx-text-fill: #FFFFFF;");
            wadah.getChildren().add(kotak);
        }
    }

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirKursi");
    }

    @FXML
    private void lanjutPembayaran() throws IOException {
        App.setRoot("fxml/kasir/KasirPembayaran");
    }
}
