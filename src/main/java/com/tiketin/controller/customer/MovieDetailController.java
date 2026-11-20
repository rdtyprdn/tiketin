
package com.tiketin.controller.customer;

import java.io.IOException;
import java.net.URL;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    public void setDataFilm(
            String judul,
            String genre,
            String durasi,
            String rating,
            String sinopsis,
            String sutradara,
            String tahun,
            String usia) {

        lblJudulFilm.setText(judul);
        lblGenre.setText(genre);
        lblDurasi.setText(durasi);
        lblRating.setText(rating);
        lblSinopsis.setText(sinopsis);
        lblSutradara.setText(sutradara);
        lblTahunRilis.setText(tahun);
        lblRatingUsia.setText(usia);
    }

    public void setPoster(String lokasiPoster) {
        URL posterUrl = getClass().getResource(lokasiPoster);

        if (posterUrl == null) {
            throw new IllegalArgumentException(
                "Poster tidak ditemukan: " + lokasiPoster
            );
        }

        imgPoster.setImage(new Image(posterUrl.toExternalForm()));
    }

    public void setPosterImage(Image poster) {
        imgPoster.setImage(poster);
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
                lblJudulFilm.getText(),
                lblGenre.getText(),
                lblDurasi.getText(),
                lblRating.getText(),
                lblSinopsis.getText(),
                lblSutradara.getText(),
                lblTahunRilis.getText(),
                lblRatingUsia.getText(),
                imgPoster.getImage()
);

            App.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
