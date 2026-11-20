
package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class KioskScheduleSelectionController {

    // INFORMASI FILM
    @FXML private ImageView imgPosterFilm;
    @FXML private Label lblJudulFilm;
    @FXML private Label lblGenre;
    @FXML private Label lblDurasi;
    @FXML private Label lblRatingUsia;
    @FXML private Label lblHargaTiket;

    // PILIHAN TANGGAL
    @FXML private VBox cardTanggal15;
    @FXML private VBox cardTanggal16;
    @FXML private VBox cardTanggal17;
    @FXML private VBox cardTanggal18;
    @FXML private VBox cardTanggal19;

    // PILIHAN JADWAL
    @FXML private VBox cardJadwal1030;
    @FXML private VBox cardJadwal1315;
    @FXML private VBox cardJadwal1600;
    @FXML private VBox cardJadwal1845;
    @FXML private VBox cardJadwal2100;

    // TOMBOL
    @FXML private Button btnLanjut;

    // DATA PILIHAN
    private String tanggalDipilih;
    private String jadwalDipilih;
    private String studioDipilih;

    // DATA FILM
    private String judulFilm;
    private String genreFilm;
    private String durasiFilm;
    private String ratingFilm;
    private String sinopsisFilm;
    private String sutradaraFilm;
    private String tahunFilm;
    private String usiaFilm;
    private Image posterFilm;

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

    @FXML
    private void initialize() {
        aturPilihanTanggal();
        aturPilihanJadwal();
        btnLanjut.setDisable(true);
    }

    // MENERIMA DATA FILM
    public void setDataFilm(
            String judul,
            String genre,
            String durasi,
            String rating,
            String sinopsis,
            String sutradara,
            String tahun,
            String usia,
            Image poster) {

        this.judulFilm = judul;
        this.genreFilm = genre;
        this.durasiFilm = durasi;
        this.ratingFilm = rating;
        this.sinopsisFilm = sinopsis;
        this.sutradaraFilm = sutradara;
        this.tahunFilm = tahun;
        this.usiaFilm = usia;
        this.posterFilm = poster;

        lblJudulFilm.setText(judul);
        lblGenre.setText(genre);
        lblDurasi.setText(durasi);
        lblRatingUsia.setText(usia);
        lblHargaTiket.setText("Rp50.000");

        if (poster != null) {
            imgPosterFilm.setImage(poster);
        }
    }

    // MEMULIHKAN PILIHAN TANGGAL DAN JADWAL
    public void restorePilihan(String tanggal, String jam, String studio) {

        tanggalDipilih = tanggal;
        jadwalDipilih = jam;
        studioDipilih = studio;

        VBox[] semuaTanggal = {
            cardTanggal15, cardTanggal16, cardTanggal17,
            cardTanggal18, cardTanggal19
        };

        for (VBox item : semuaTanggal) {
            item.setStyle(BORDER_NORMAL_TANGGAL);
        }

        if ("15 November 2026".equals(tanggal)) {
            cardTanggal15.setStyle(BORDER_SELECTED_TANGGAL);
        } else if ("16 November 2026".equals(tanggal)) {
            cardTanggal16.setStyle(BORDER_SELECTED_TANGGAL);
        } else if ("17 November 2026".equals(tanggal)) {
            cardTanggal17.setStyle(BORDER_SELECTED_TANGGAL);
        } else if ("18 November 2026".equals(tanggal)) {
            cardTanggal18.setStyle(BORDER_SELECTED_TANGGAL);
        } else if ("19 November 2026".equals(tanggal)) {
            cardTanggal19.setStyle(BORDER_SELECTED_TANGGAL);
        }

        VBox[] semuaJadwal = {
            cardJadwal1030, cardJadwal1315,
            cardJadwal1600, cardJadwal1845,
            cardJadwal2100
        };

        for (VBox item : semuaJadwal) {
            item.setStyle(BORDER_NORMAL_JADWAL);
        }

        if ("10.30".equals(jam)) {
            cardJadwal1030.setStyle(BORDER_SELECTED_JADWAL);
        } else if ("13.15".equals(jam)) {
            cardJadwal1315.setStyle(BORDER_SELECTED_JADWAL);
        } else if ("16.00".equals(jam)) {
            cardJadwal1600.setStyle(BORDER_SELECTED_JADWAL);
        } else if ("18.45".equals(jam)) {
            cardJadwal1845.setStyle(BORDER_SELECTED_JADWAL);
        } else if ("21.00".equals(jam)) {
            cardJadwal2100.setStyle(BORDER_SELECTED_JADWAL);
        }

        periksaPilihan();
    }

    // MENGATUR PILIHAN TANGGAL
    private void aturPilihanTanggal() {
        pasangTanggal(cardTanggal15, "15 November 2026");
        pasangTanggal(cardTanggal16, "16 November 2026");
        pasangTanggal(cardTanggal17, "17 November 2026");
        pasangTanggal(cardTanggal18, "18 November 2026");
        pasangTanggal(cardTanggal19, "19 November 2026");
    }

    private void pasangTanggal(VBox kartu, String tanggal) {
        kartu.setOnMouseClicked(event -> {

            tanggalDipilih = tanggal;

            VBox[] semua = {
                cardTanggal15, cardTanggal16, cardTanggal17,
                cardTanggal18, cardTanggal19
            };

            for (VBox item : semua) {
                item.setStyle(BORDER_NORMAL_TANGGAL);
            }

            kartu.setStyle(BORDER_SELECTED_TANGGAL);
            System.out.println("TINGGI KARTU: " + kartu.getHeight());
            System.out.println("PREFERRED HEIGHT: " + kartu.getPrefHeight());
            periksaPilihan();
        });
    }

    // MENGATUR PILIHAN JADWAL
    private void aturPilihanJadwal() {
        pasangJadwal(cardJadwal1030, "10.30", "Studio 1");
        pasangJadwal(cardJadwal1315, "13.15", "Studio 2");
        pasangJadwal(cardJadwal1600, "16.00", "Studio 1");
        pasangJadwal(cardJadwal1845, "18.45", "Studio 3");
        pasangJadwal(cardJadwal2100, "21.00", "Studio 2");
    }

    private void pasangJadwal(VBox kartu, String jam, String studio) {
        kartu.setOnMouseClicked(event -> {

            jadwalDipilih = jam;
            studioDipilih = studio;

            VBox[] semua = {
                cardJadwal1030, cardJadwal1315,
                cardJadwal1600, cardJadwal1845,
                cardJadwal2100
            };

            for (VBox item : semua) {
                item.setStyle(BORDER_NORMAL_JADWAL);
            }

            kartu.setStyle(BORDER_SELECTED_JADWAL);
            periksaPilihan();
        });
    }

    // MEMERIKSA PILIHAN
    private void periksaPilihan() {
        btnLanjut.setDisable(
            tanggalDipilih == null || jadwalDipilih == null
        );
    }

    // KEMBALI KE HALAMAN FILM
    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/movie-list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LANJUT KE RINGKASAN PESANAN
    @FXML
    private void handleLanjut() {

        if (tanggalDipilih == null || jadwalDipilih == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/com/tiketin/fxml/customer/kiosk-order-summary.fxml"
                )
            );

            Parent root = loader.load();

            KioskOrderSummaryController controller =
                loader.getController();

            controller.setDataPesanan(
                judulFilm,
                genreFilm,
                durasiFilm,
                ratingFilm,
                sinopsisFilm,
                sutradaraFilm,
                tahunFilm,
                usiaFilm,
                tanggalDipilih,
                jadwalDipilih,
                studioDipilih,
                posterFilm
            );

            App.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
