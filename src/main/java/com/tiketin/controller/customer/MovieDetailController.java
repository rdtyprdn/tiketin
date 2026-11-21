package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.service.FilmService;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.KioskSession;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class MovieDetailController {

    @FXML
    private Label lblJudulFilm;

    @FXML
    private Label lblGenre;

    @FXML
    private Label lblDurasi;

    @FXML
    private Label lblRating;

    @FXML
    private Label lblSinopsis;

    @FXML
    private Label lblSutradara;

    @FXML
    private Label lblTahunRilis;

    @FXML
    private Label lblRatingUsia;

    @FXML
    private ImageView imgPoster;

    // Data film diambil dari film yang dipilih di halaman daftar film (database)
    @FXML
    private void initialize() {
        Film film = KioskSession.getFilmTerpilih();
        if (film == null) {
            return; // dibuka tanpa memilih film: biarkan tampilan contoh
        }
        lblJudulFilm.setText(film.getJudul());
        lblGenre.setText(teks(film.getGenre()));
        lblDurasi.setText(film.getDurasi() + " min");
        lblSinopsis.setText(teks(film.getSinopsis()));
        lblSinopsis.setWrapText(true);
        imgPoster.setImage(KasirUtil.poster(film));

        // Detail film diisi admin di Admin > Film; "-" kalau belum diisi
        lblRating.setText(FilmService.formatRating(film.getRating()));
        lblSutradara.setText(teks(film.getSutradara()));
        lblTahunRilis.setText(film.getTahunRilis() == null ? "-" : String.valueOf(film.getTahunRilis()));
        lblRatingUsia.setText(teks(film.getRatingUsia()));
    }

    private String teks(String nilai) {
        return nilai == null || nilai.isBlank() ? "-" : nilai;
    }

    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/movie-list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePilihFilm() {
        if (KioskSession.getFilmTerpilih() == null) {
            return;
        }
        try {
            App.setRoot("fxml/customer/kiosk-schedule-selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
