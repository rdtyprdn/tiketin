package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Kasir;
import com.tiketin.model.KasirRiwayatItem;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class KasirRiwayatController {

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    @FXML
    private Label lblTotalTransaksi;

    @FXML
    private Label lblTiketTerjual;

    @FXML
    private Label lblTotalPenjualan;

    @FXML
    private TextField txtCariTransaksi;

    @FXML
    private DatePicker dpTanggalTransaksi;

    @FXML
    private TableView<KasirRiwayatItem> tableRiwayat;

    @FXML
    private TableColumn<KasirRiwayatItem, String> colIdTransaksi;

    @FXML
    private TableColumn<KasirRiwayatItem, String> colTanggal;

    @FXML
    private TableColumn<KasirRiwayatItem, String> colFilm;

    @FXML
    private TableColumn<KasirRiwayatItem, Integer> colJumlahTiket;

    @FXML
    private TableColumn<KasirRiwayatItem, String> colTotal;

    @FXML
    private TableColumn<KasirRiwayatItem, String> colStatus;

    @FXML
    private TableColumn<KasirRiwayatItem, Void> colAksi;

    private FilteredList<KasirRiwayatItem> dataFilter;

    private final KasirTransaksiService service = new KasirTransaksiService();

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);

        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("dd MMM yyyy, HH.mm", Locale.forLanguageTag("id-ID"));

        colIdTransaksi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdTransaksi()));
        colTanggal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggal().format(formatTanggal)));
        colFilm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFilm()));
        colJumlahTiket.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getJumlahTiket()).asObject());
        colTotal.setCellValueFactory(data -> new SimpleStringProperty(KasirUtil.rupiah(data.getValue().getTotal())));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        buatTombolDetail();

        ObservableList<KasirRiwayatItem> semua = muatRiwayat();
        hitungStatistik(semua);

        // Filter transaksi
        dataFilter = new FilteredList<>(semua, data -> true);
        tableRiwayat.setItems(dataFilter);

        txtCariTransaksi.textProperty().addListener((observable, oldValue, newValue) -> filterTransaksi());
        dpTanggalTransaksi.valueProperty().addListener((observable, oldValue, newValue) -> filterTransaksi());
        filterTransaksi();
    }

    // Riwayat dari database, hanya transaksi kasir yang login
    private ObservableList<KasirRiwayatItem> muatRiwayat() {
        ObservableList<KasirRiwayatItem> daftar = FXCollections.observableArrayList();
        Kasir kasir = KasirSession.getKasirLogin();
        if (kasir != null) {
            try {
                daftar.addAll(service.riwayat(kasir.getIdKasir()));
            } catch (IllegalArgumentException ex) {
                Peringatan.gagal(ex.getMessage());
            }
        }
        return daftar;
    }

    // Hanya transaksi lunas yang dihitung
    private void hitungStatistik(ObservableList<KasirRiwayatItem> daftar) {
        int totalTransaksi = 0;
        int totalTiket = 0;
        double totalPenjualan = 0;

        for (KasirRiwayatItem transaksi : daftar) {
            if ("Lunas".equals(transaksi.getStatus())) {
                totalTransaksi++;
                totalTiket += transaksi.getJumlahTiket();
                totalPenjualan += transaksi.getTotal();
            }
        }

        lblTotalTransaksi.setText(totalTransaksi + " Transaksi");
        lblTiketTerjual.setText(totalTiket + " Tiket");
        lblTotalPenjualan.setText(KasirUtil.rupiah(totalPenjualan));
    }

    private void filterTransaksi() {
        String kataKunci = txtCariTransaksi.getText() == null ? "" : txtCariTransaksi.getText().toLowerCase().trim();
        LocalDate tanggal = dpTanggalTransaksi.getValue();

        dataFilter.setPredicate(transaksi -> {
            boolean cocokPencarian = transaksi.getIdTransaksi().toLowerCase().contains(kataKunci)
                || transaksi.getFilm().toLowerCase().contains(kataKunci);
            boolean cocokTanggal = tanggal == null || transaksi.getTanggal().toLocalDate().equals(tanggal);
            return cocokPencarian && cocokTanggal;
        });
    }

    private void buatTombolDetail() {
        colAksi.setCellFactory(col -> new TableCell<KasirRiwayatItem, Void>() {

            private final Button btnDetail = new Button("Detail");

            {
                btnDetail.setStyle(
                    "-fx-background-color: #E5EBF5;" +
                    "-fx-text-fill: #1A2C50;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
                );
                btnDetail.setOnAction(event -> {
                    KasirRiwayatItem transaksi = getTableRow().getItem();
                    if (transaksi != null) {
                        tampilkanDetail(transaksi);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDetail);
            }
        });
    }

    private void tampilkanDetail(KasirRiwayatItem transaksi) {
        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH.mm", Locale.forLanguageTag("id-ID"));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Transaksi");
        alert.setHeaderText(transaksi.getIdTransaksi());
        alert.setContentText(
            "Film: " + transaksi.getFilm() +
            "\nTanggal: " + transaksi.getTanggal().format(formatTanggal) +
            "\nJumlah Tiket: " + transaksi.getJumlahTiket() +
            "\nTotal: " + KasirUtil.rupiah(transaksi.getTotal()) +
            "\nStatus: " + transaksi.getStatus()
        );
        alert.showAndWait();
    }

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirHome");
    }
}
