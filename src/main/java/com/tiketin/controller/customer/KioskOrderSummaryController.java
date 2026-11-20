
package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class KioskOrderSummaryController {

    // DETAIL FILM
    @FXML private ImageView imgPosterSummary;
    @FXML private Label lblJudulFilmSummary;
    @FXML private Label lblTanggalSummary;
    @FXML private Label lblJamSummary;
    @FXML private Label lblStudioSummary;

    // RINGKASAN PESANAN
    @FXML private Label lblJumlahTiketSummary;
    @FXML private Label lblHargaTiketSummary;
    @FXML private Label lblTotalPembayaran;

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

    // DATA JADWAL
    private String tanggalDipilih;
    private String jadwalDipilih;
    private String studioDipilih;

    // HARGA TIKET
    private final int hargaTiket = 50000;
    private final int jumlahTiket = 2;

    private int totalPembayaran;

    // MENERIMA DATA DARI HALAMAN JADWAL
    public void setDataPesanan(
            String judul,
            String genre,
            String durasi,
            String rating,
            String sinopsis,
            String sutradara,
            String tahun,
            String usia,
            String tanggal,
            String jam,
            String studio,
            Image poster) {

        // SIMPAN DATA FILM
        this.judulFilm = judul;
        this.genreFilm = genre;
        this.durasiFilm = durasi;
        this.ratingFilm = rating;
        this.sinopsisFilm = sinopsis;
        this.sutradaraFilm = sutradara;
        this.tahunFilm = tahun;
        this.usiaFilm = usia;
        this.posterFilm = poster;

        // SIMPAN DATA JADWAL
        this.tanggalDipilih = tanggal;
        this.jadwalDipilih = jam;
        this.studioDipilih = studio;

        // TAMPILKAN DATA FILM DAN JADWAL
        lblJudulFilmSummary.setText(judul);
        lblTanggalSummary.setText(tanggal);
        lblJamSummary.setText(jam);
        lblStudioSummary.setText(studio);

        if (poster != null) {
            imgPosterSummary.setImage(poster);
        }

        // HITUNG TOTAL PEMBAYARAN
        totalPembayaran = hargaTiket * jumlahTiket;

        lblJumlahTiketSummary.setText(jumlahTiket + " Tiket");
        lblHargaTiketSummary.setText("Rp50.000");

        lblTotalPembayaran.setText(
            "Rp" + String.format("%,d", totalPembayaran).replace(',', '.')
        );
    }

    // KEMBALI KE HALAMAN PEMILIHAN JADWAL
    @FXML
    private void handleKembali() {

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/com/tiketin/fxml/customer/kiosk-schedule-selection.fxml"
                )
            );

            Parent root = loader.load();

            KioskScheduleSelectionController controller =
                loader.getController();

            controller.setDataFilm(
                judulFilm,
                genreFilm,
                durasiFilm,
                ratingFilm,
                sinopsisFilm,
                sutradaraFilm,
                tahunFilm,
                usiaFilm,
                posterFilm
            );

            controller.restorePilihan(
                tanggalDipilih,
                jadwalDipilih,
                studioDipilih
            );

            App.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LANJUT KE PEMBAYARAN QRIS
    
    @FXML
    private void handleLanjutPembayaran() {
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/com/tiketin/fxml/customer/kiosk-qris-simulation.fxml"
            )
        );

        Parent root = loader.load();

        KioskQRISController controller = loader.getController();

        controller.setDataPesanan(
            totalPembayaran,
            judulFilm,
            tanggalDipilih,
            jadwalDipilih,
            studioDipilih
        );

        App.setRoot(root);

        } catch (IOException e) {
         e.printStackTrace();
        }
    }
}
