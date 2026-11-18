package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.BarisTransaksi;
import com.tiketin.service.TransaksiService;
import com.tiketin.util.Rupiah;
import com.tiketin.util.Tabel;
import com.tiketin.util.Waktu;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TransaksiController implements HalamanAdmin {
    @FXML
    private TextField cariField;
    @FXML
    private TableView<BarisTransaksi> tabel;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomId;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomWaktu;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomFilm;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomKanal;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomKasir;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomTotal;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomStatus;
    @FXML
    private Label detailJudul;
    @FXML
    private Label detailJadwal;
    @FXML
    private Label detailStudio;
    @FXML
    private Label detailKursi;
    @FXML
    private Label detailKanal;
    @FXML
    private Label detailTotal;

    private final TransaksiService transaksiService = new TransaksiService();

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdPemesanan())));
        kolomWaktu.setCellValueFactory(data -> new SimpleStringProperty(Waktu.format(data.getValue().getWaktu())));
        kolomFilm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJudulFilm()));
        kolomKanal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKanal().getLabel()));
        kolomKasir.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaKasir()));
        kolomTotal.setCellValueFactory(data -> new SimpleStringProperty(Rupiah.dari(data.getValue().getTotalBayar())));
        Tabel.chip(kolomStatus, baris -> baris.getStatus().getLabel(), TransaksiController::gayaStatus);
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> isiDetail(baru));
        cariField.textProperty().addListener((obs, lama, baru) -> muat());
        isiDetail(null);
    }

    @Override
    public void siap(Admin admin) {
        muat();
    }

    private void muat() {
        tabel.getItems().setAll(transaksiService.cari(cariField.getText()));
        isiDetail(tabel.getSelectionModel().getSelectedItem());
    }

    private void isiDetail(BarisTransaksi baris) {
        if (baris == null) {
            detailJudul.setText("Pilih transaksi");
            detailJadwal.setText("Detail pesanan muncul di sini.");
            detailStudio.setText("");
            detailKursi.setText("");
            detailKanal.setText("");
            detailTotal.setText("");
            return;
        }
        detailJudul.setText(baris.getJudulFilm());
        detailJadwal.setText("Tayang " + baris.getWaktuTayang());
        detailStudio.setText(baris.getNamaStudio());
        detailKursi.setText("Kursi " + baris.getKursi());
        detailKanal.setText(baris.getKanal().getLabel() + " · " + baris.getNamaKasir() + " · " + baris.getStatus().getLabel());
        detailTotal.setText(Rupiah.dari(baris.getTotalBayar()));
    }

    private static String gayaStatus(BarisTransaksi baris) {
        return switch (baris.getStatus()) {
            case BERHASIL -> "berhasil";
            case DIBATALKAN -> "batal";
            case MENUNGGU_PEMBAYARAN -> "tunggu";
        };
    }
}
