package com.tiketin.controller.kasir;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.tiketin.model.Film;
import com.tiketin.util.KasirSession;
import javafx.scene.image.Image;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.tiketin.model.KasirTiketItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import java.text.NumberFormat;
import com.tiketin.App;
import java.io.IOException;

public class KasirTiketController {

    @FXML
    private Label lblDeskripsiBerhasil;

    @FXML
    private ImageView imgPosterFilm;

    @FXML
    private Label lblJudulFilm;

    @FXML
    private Label lblGenreDurasi;

    @FXML
    private Label lblJadwalFilm;

    @FXML
    private Label lblTotalPembayaran;

    @FXML
    private TableView<KasirTiketItem> tableTiket;
    
    @FXML
    private TableColumn<KasirTiketItem, Integer> colNo;
    
    @FXML
    private TableColumn<KasirTiketItem, String> colKursi;
    
    @FXML
    private TableColumn<KasirTiketItem, String> colKodeTiket;
    
    @FXML
    private TableColumn<KasirTiketItem, String> colHarga;

    @FXML
    private void initialize() {
        tampilkanTiket();
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

        lblJadwalFilm.setText(
            KasirSession.getTanggalTerpilih().format(format)
            + " · " + KasirSession.getJamTerpilih()
            + " · " + KasirSession.getStudioTerpilih()
        );
    }

    lblDeskripsiBerhasil.setText(
        "Simulasi pembayaran berhasil."
    );
    }

    private void tampilkanTiket() {

    NumberFormat format = NumberFormat.getNumberInstance(
        Locale.forLanguageTag("id-ID")
    );

    colNo.setCellValueFactory(data ->
        new SimpleIntegerProperty(
            data.getValue().getNo()
        ).asObject()
    );

    colKursi.setCellValueFactory(data ->
        new SimpleStringProperty(
            data.getValue().getKursi()
        )
    );

    colKodeTiket.setCellValueFactory(data ->
        new SimpleStringProperty(
            data.getValue().getKodeTiket()
        )
    );

    colHarga.setCellValueFactory(data ->
        new SimpleStringProperty(
            "Rp" + format.format(data.getValue().getHarga())
        )
    );

    tableTiket.setItems(FXCollections.observableArrayList(
        new KasirTiketItem(1, "D5", "DEMO-001", 50000),
        new KasirTiketItem(2, "D6", "DEMO-002", 50000)
    ));

    lblTotalPembayaran.setText("Rp100.000");
    }

    @FXML
    private void kembaliHome() throws IOException {
    KasirSession.reset();
    App.setRoot("fxml/kasir/KasirHome");
    }
}