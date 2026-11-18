package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.util.KasirSession;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private void initialize() {

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
    
    @FXML
    private void kembali() throws IOException {
    App.setRoot("fxml/kasir/KasirJadwal");
    }
    
    @FXML
    private void lanjutPembayaran() throws IOException {
    App.setRoot("fxml/kasir/KasirPembayaran");
    }
}