package com.tiketin.controller.customer;

import java.io.IOException;
import java.util.List;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.KioskSession;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MovieListController {

    @FXML
    private HBox wadahFilm;

    private final KasirTransaksiService service = new KasirTransaksiService();

    @FXML
    private void initialize() {
        wadahFilm.getChildren().clear();

        List<Film> daftar;
        try {
            daftar = service.daftarFilm();
        } catch (IllegalArgumentException ex) {
            wadahFilm.getChildren().add(pesan("Film tidak bisa dimuat: " + ex.getMessage()));
            return;
        }
        if (daftar.isEmpty()) {
            wadahFilm.getChildren().add(pesan("Belum ada film yang tayang."));
            return;
        }

        // Kartu disusun dalam satu baris; kalau film lebih banyak dari lebar layar bisa digeser
        HBox baris = new HBox(20);
        baris.setAlignment(Pos.CENTER);
        for (Film film : daftar) {
            baris.getChildren().add(buatKartu(film));
        }

        ScrollPane gulir = new ScrollPane(baris);
        gulir.setFitToHeight(true);
        gulir.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        gulir.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gulir.setMinHeight(420);
        gulir.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        // supaya kartu tetap di tengah kalau filmnya sedikit
        baris.minWidthProperty().bind(gulir.widthProperty().subtract(4));
        HBox.setHgrow(gulir, Priority.ALWAYS);
        wadahFilm.getChildren().add(gulir);
    }

    // Kartu film, gayanya disamakan dengan kartu di desain Alia
    private VBox buatKartu(Film film) {
        VBox kartu = new VBox(10);
        kartu.setPrefSize(220, 390);
        kartu.setMinWidth(220);
        kartu.setMinHeight(390);
        kartu.setMaxHeight(390);
        kartu.setPadding(new Insets(12));
        kartu.setStyle("-fx-background-radius: 16; -fx-background-color: #FFFFFF;");

        // Poster dijaga rasionya dan ditaruh di tengah kotak 196x220 supaya tidak gepeng
        ImageView gambar = new ImageView(KasirUtil.poster(film));
        gambar.setFitWidth(196);
        gambar.setFitHeight(220);
        gambar.setPreserveRatio(true);
        gambar.setSmooth(true);
        StackPane poster = new StackPane(gambar);
        poster.setPrefSize(196, 220);
        poster.setMinSize(196, 220);
        poster.setMaxSize(196, 220);

        Label judul = new Label(film.getJudul());
        judul.setTextFill(Color.web("#1a2c50"));
        judul.setFont(new Font("Inter SemiBold", 16));
        judul.setMaxWidth(196);
        judul.setTooltip(new Tooltip(film.getJudul()));

        String genre = film.getGenre() == null || film.getGenre().isBlank() ? "-" : film.getGenre();
        Label info = new Label(genre + " • " + film.getDurasi() + " min");
        info.setTextFill(Color.web("#1a2c50"));
        info.setFont(new Font("Inter Regular", 12));
        info.setMaxWidth(196);

        VBox pengisi = new VBox();
        VBox.setVgrow(pengisi, Priority.ALWAYS);

        Button pilih = new Button("Pilih Film");
        pilih.setPrefSize(193, 24);
        pilih.setMaxWidth(196);
        pilih.setStyle("-fx-background-color: #FBC436; -fx-background-radius: 10; -fx-text-fill: #1A2C50; "
                + "-fx-font-family: \"Inter\"; -fx-font-weight: bold; -fx-font-size: 13;");
        pilih.setOnAction(e -> bukaDetailFilm(film));

        kartu.getChildren().addAll(poster, judul, info, pengisi, pilih);
        return kartu;
    }

    private Label pesan(String teks) {
        Label label = new Label(teks);
        label.setTextFill(Color.web("#1a2c50"));
        label.setFont(new Font("Inter SemiBold", 16));
        label.setWrapText(true);
        return label;
    }

    private void bukaDetailFilm(Film film) {
        KioskSession.setFilmTerpilih(film);
        try {
            App.setRoot("fxml/customer/movie-detail");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/kiosk-home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
