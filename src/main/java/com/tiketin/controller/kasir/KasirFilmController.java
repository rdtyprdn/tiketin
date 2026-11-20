package com.tiketin.controller.kasir;

import java.io.IOException;
import java.util.List;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class KasirFilmController {

    @FXML
    private HBox containerFilm;

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    private final KasirTransaksiService service = new KasirTransaksiService();

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);
        tampilkanFilm();
    }

    // Ganti kartu film dummy di FXML dengan film dari database
    private void tampilkanFilm() {
        FlowPane wadah = new FlowPane(24, 20);
        wadah.setAlignment(Pos.TOP_CENTER);
        wadah.setPrefWrapLength(1000);
        HBox.setHgrow(wadah, Priority.ALWAYS);
        containerFilm.getChildren().setAll(wadah);

        List<Film> daftarFilm;
        try {
            daftarFilm = service.daftarFilm();
        } catch (IllegalArgumentException ex) {
            Peringatan.gagal(ex.getMessage());
            return;
        }

        if (daftarFilm.isEmpty()) {
            Label kosong = new Label("Belum ada film. Tambahkan film lewat halaman Admin.");
            kosong.setTextFill(Color.web("#64748b"));
            wadah.getChildren().add(kosong);
            return;
        }

        for (Film film : daftarFilm) {
            wadah.getChildren().add(buatKartu(film));
        }
    }

    // Kartu film, gaya sama dengan desain Visvani
    private VBox buatKartu(Film film) {
        ImageView poster = new ImageView(KasirUtil.poster(film));
        poster.setFitWidth(200);
        poster.setFitHeight(300);
        poster.setPreserveRatio(true);

        Label judul = new Label(film.getJudul());
        judul.setTextFill(Color.web("#1a2c50"));
        judul.setFont(Font.font("Inter 18pt SemiBold", 16));
        judul.setWrapText(true);
        judul.setMaxWidth(226);

        Label info = new Label(film.getGenre() + " · " + film.getDurasi() + " menit");
        info.setTextFill(Color.web("#64748b"));
        info.setFont(Font.font("Inter 28pt Regular", 12));

        Button tombol = new Button("Pilih Film");
        tombol.setPrefSize(206, 34);
        tombol.setTextFill(Color.web("#1a2c50"));
        tombol.setFont(Font.font("Inter 28pt SemiBold", 14));
        tombol.setStyle("-fx-background-color: #FBC436; -fx-background-radius: 9; -fx-cursor: hand;");
        tombol.setUserData(film);
        tombol.setOnAction(this::pilihFilm);

        VBox kartu = new VBox(8, poster, judul, info, tombol);
        kartu.setPrefWidth(250);
        kartu.setMaxWidth(250);
        kartu.setStyle("-fx-padding: 12; -fx-background-color: #FFFFFF; -fx-background-radius: 12;");
        return kartu;
    }

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirHome");
    }

    // Dipanggil tombol "Pilih Film" (film disimpan di userData tombol)
    @FXML
    private void pilihFilm(ActionEvent event) {
        Button tombol = (Button) event.getSource();
        if (!(tombol.getUserData() instanceof Film film)) {
            return;
        }
        KasirSession.reset();
        KasirSession.setFilmTerpilih(film);
        try {
            App.setRoot("fxml/kasir/KasirJadwal");
        } catch (IOException ex) {
            ex.printStackTrace();
            Peringatan.gagal("Halaman jadwal tidak bisa dibuka: " + ex.getMessage());
        }
    }
}
