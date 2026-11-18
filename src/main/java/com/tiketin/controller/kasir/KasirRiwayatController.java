package com.tiketin.controller.kasir;

import com.tiketin.model.KasirRiwayatItem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Alert;
import com.tiketin.App;
import java.io.IOException;
import com.tiketin.util.KasirRiwayatData;

public class KasirRiwayatController {

    @FXML
    private Label lblIdKasir;

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

    @FXML
    private void initialize() {
        tableRiwayat.setItems(
            KasirRiwayatData.getDaftarTransaksi()
        );

        buatTombolDetail();
        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern(
            "dd MMM yyyy, HH.mm",
            Locale.forLanguageTag("id-ID")
        );

        NumberFormat formatHarga = NumberFormat.getNumberInstance(
            Locale.forLanguageTag("id-ID")
        );

        colIdTransaksi.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getIdTransaksi()
            )
        );

        colTanggal.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getTanggal().format(formatTanggal)
            )
        );

        colFilm.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getFilm()
            )
        );

        colJumlahTiket.setCellValueFactory(data ->
            new SimpleIntegerProperty(
                data.getValue().getJumlahTiket()
            ).asObject()
        );

        colTotal.setCellValueFactory(data ->
            new SimpleStringProperty(
                "Rp" + formatHarga.format(data.getValue().getTotal())
            )
        );

        colStatus.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getStatus()
            )
        );

        tableRiwayat.setItems(
    KasirRiwayatData.getDaftarTransaksi()
    );

        hitungStatistik();

        // Filter transaksi
        dataFilter = new FilteredList<>(
            tableRiwayat.getItems(), data -> true
        );

        tableRiwayat.setItems(dataFilter);

        txtCariTransaksi.textProperty().addListener(
            (observable, oldValue, newValue) -> filterTransaksi()
        );

        dpTanggalTransaksi.valueProperty().addListener(
            (observable, oldValue, newValue) -> filterTransaksi()
        );

        filterTransaksi();
    }

    private void hitungStatistik() {

        int totalTransaksi = tableRiwayat.getItems().size();
        int totalTiket = 0;
        int totalPenjualan = 0;

        for (KasirRiwayatItem transaksi : tableRiwayat.getItems()) {
            totalTiket += transaksi.getJumlahTiket();
            totalPenjualan += transaksi.getTotal();
        }

        NumberFormat format = NumberFormat.getNumberInstance(
            Locale.forLanguageTag("id-ID")
        );

        lblTotalTransaksi.setText(
            String.valueOf(totalTransaksi)
        );

        lblTiketTerjual.setText(
            String.valueOf(totalTiket)
        );

        lblTotalPenjualan.setText(
            "Rp" + format.format(totalPenjualan)
        );
    }

    private void filterTransaksi() {

        String kataKunci = txtCariTransaksi.getText()
            .toLowerCase().trim();

        LocalDate tanggal = dpTanggalTransaksi.getValue();

        dataFilter.setPredicate(transaksi -> {

            boolean cocokPencarian =
                transaksi.getIdTransaksi()
                    .toLowerCase().contains(kataKunci)
                || transaksi.getFilm()
                    .toLowerCase().contains(kataKunci);

            boolean cocokTanggal =
                tanggal == null
                || transaksi.getTanggal().toLocalDate().equals(tanggal);

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

    NumberFormat format = NumberFormat.getNumberInstance(
        Locale.forLanguageTag("id-ID")
    );

    DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern(
        "dd MMMM yyyy, HH.mm",
        Locale.forLanguageTag("id-ID")
    );

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    alert.setTitle("Detail Transaksi");
    alert.setHeaderText(transaksi.getIdTransaksi());

    alert.setContentText(
        "Film: " + transaksi.getFilm() +
        "\nTanggal: " + transaksi.getTanggal().format(formatTanggal) +
        "\nJumlah Tiket: " + transaksi.getJumlahTiket() +
        "\nTotal: Rp" + format.format(transaksi.getTotal()) +
        "\nStatus: " + transaksi.getStatus()
    );

    alert.showAndWait();
 }

 @FXML
    private void kembali() throws IOException {
    App.setRoot("fxml/kasir/KasirHome");
    }

}