package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.dao.PemesananDAO;
import com.tiketin.model.Film;
import com.tiketin.model.KasirTiketItem;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

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
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

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

    private final KasirTransaksiService service = new KasirTransaksiService();

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
            lblJadwalFilm.setText(KasirSession.getTanggalTerpilih().format(format)
                + " · " + KasirSession.getJamTerpilih()
                + " · " + KasirSession.getStudioTerpilih());
        }

        tampilkanTiket();
    }

    // Tiket diambil dari database berdasarkan id pemesanan terakhir
    private void tampilkanTiket() {
        colNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNo()).asObject());
        colKursi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKursi()));
        colKodeTiket.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKodeTiket()));
        colHarga.setCellValueFactory(data -> new SimpleStringProperty(KasirUtil.rupiah(data.getValue().getHarga())));

        Integer idPemesanan = KasirSession.getIdPemesananTerakhir();
        List<KasirTiketItem> daftar = new ArrayList<>();
        if (idPemesanan != null) {
            try {
                daftar = service.tiket(idPemesanan);
            } catch (IllegalArgumentException ex) {
                Peringatan.gagal(ex.getMessage());
            }
        }
        tableTiket.setItems(FXCollections.observableArrayList(daftar));

        double total = 0;
        for (KasirTiketItem item : daftar) {
            total += item.getHarga();
        }
        lblTotalPembayaran.setText(KasirUtil.rupiah(total));

        String kode = idPemesanan == null ? "-" : PemesananDAO.kodeTransaksi(idPemesanan);
        lblDeskripsiBerhasil.setText("Transaksi " + kode + " berhasil, " + daftar.size() + " tiket telah diterbitkan.");
    }

    @FXML
    private void kembaliHome() throws IOException {
        KasirSession.reset();
        App.setRoot("fxml/kasir/KasirHome");
    }
}
