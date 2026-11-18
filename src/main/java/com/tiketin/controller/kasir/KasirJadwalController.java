package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.util.KasirSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class KasirJadwalController {

    @FXML
    private ImageView imgPosterFilm;

    @FXML
    private Label lblJudulFilm;

    @FXML
    private Label lblGenreDurasi;

    @FXML
    private Label lblSinopsis;

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
    private VBox cardJadwal1;

    @FXML
    private VBox cardJadwal2;

    @FXML
    private VBox cardJadwal3;

    @FXML
    private VBox cardJadwal4;

    @FXML
    private VBox cardJadwal5;

    // Menampilkan informasi film
    @FXML
    private void initialize() {

        tampilkanTanggal();

        Film film = KasirSession.getFilmTerpilih();

        if (film != null) {

            lblJudulFilm.setText(film.getJudul());

            lblGenreDurasi.setText(
                film.getGenre() + " · "
                + film.getDurasi() + " menit"
            );

            lblSinopsis.setText(film.getSinopsis());

            imgPosterFilm.setImage(new Image(
                getClass().getResourceAsStream(
                    "/com/tiketin/images/" + film.getPoster()
                )
            ));
        }
    }

    // Kembali ke halaman pemilihan film
    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirFilm");
    }

    // Menampilkan tanggal secara otomatis
    private void tampilkanTanggal() {

        LocalDate hariIni = LocalDate.now();

        DateTimeFormatter format = DateTimeFormatter.ofPattern(
            "EEE, dd MMM yyyy",
            Locale.forLanguageTag("id-ID")
        );

        Button[] tombolTanggal = {
            btnTanggal1,
            btnTanggal2,
            btnTanggal3,
            btnTanggal4,
            btnTanggal5
        };

        for (int i = 0; i < tombolTanggal.length; i++) {

            LocalDate tanggal = hariIni.plusDays(i);

            tombolTanggal[i].setText(tanggal.format(format));
            tombolTanggal[i].setUserData(tanggal);
        }

        // Memilih tanggal pertama secara otomatis
        pilihTanggal(new ActionEvent(btnTanggal1, btnTanggal1));
    }

    // Memilih tanggal tayang
    @FXML
    private void pilihTanggal(ActionEvent event) {

        Button tombol = (Button) event.getSource();

        LocalDate tanggalTerpilih =
            (LocalDate) tombol.getUserData();

        KasirSession.setTanggalTerpilih(tanggalTerpilih);

        Button[] tombolTanggal = {
            btnTanggal1,
            btnTanggal2,
            btnTanggal3,
            btnTanggal4,
            btnTanggal5
        };

        for (Button btn : tombolTanggal) {
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
        KasirSession.setJamTerpilih(null);
        KasirSession.setStudioTerpilih(null);

        resetWarnaJadwal();
    }

    // Memilih jam dan studio
    @FXML
    private void pilihJadwal(MouseEvent event) {

        VBox card = (VBox) event.getSource();

        switch (card.getId()) {

            case "cardJadwal1":
                KasirSession.setJamTerpilih("10.30");
                KasirSession.setStudioTerpilih("Studio 1");
                break;

            case "cardJadwal2":
                KasirSession.setJamTerpilih("13.15");
                KasirSession.setStudioTerpilih("Studio 2");
                break;

            case "cardJadwal3":
                KasirSession.setJamTerpilih("16.00");
                KasirSession.setStudioTerpilih("Studio 1");
                break;

            case "cardJadwal4":
                KasirSession.setJamTerpilih("18.45");
                KasirSession.setStudioTerpilih("Studio 3");
                break;

            case "cardJadwal5":
                KasirSession.setJamTerpilih("21.00");
                KasirSession.setStudioTerpilih("Studio 2");
                break;

            default:
                return;
        }

        resetWarnaJadwal();

        // Warna card yang dipilih
        card.setStyle(
            "-fx-background-color: #E5EBF5;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #1A2C50;" +
            "-fx-border-radius: 12;"
        );
    }

    // Mengembalikan semua card jadwal ke warna awal
    private void resetWarnaJadwal() {

        VBox[] daftarCard = {
            cardJadwal1,
            cardJadwal2,
            cardJadwal3,
            cardJadwal4,
            cardJadwal5
        };

        for (VBox item : daftarCard) {
            item.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
            );
        }
    }

    @FXML
    private void lanjut() throws IOException {

    if (KasirSession.getTanggalTerpilih() == null ||
        KasirSession.getJamTerpilih() == null ||
        KasirSession.getStudioTerpilih() == null) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Jadwal Belum Dipilih");
        alert.setHeaderText(null);
        alert.setContentText(
            "Silakan pilih tanggal dan jadwal tayang terlebih dahulu."
        );
        alert.showAndWait();

        return;
    }

        App.setRoot("fxml/kasir/KasirRingkasan");
    }
}