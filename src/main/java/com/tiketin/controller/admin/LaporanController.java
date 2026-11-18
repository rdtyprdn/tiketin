package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.BarisTransaksi;
import com.tiketin.model.RingkasanLaporan;
import com.tiketin.service.LaporanService;
import com.tiketin.util.Rupiah;
import com.tiketin.util.Tabel;
import com.tiketin.util.Waktu;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class LaporanController implements HalamanAdmin {
    @FXML
    private DatePicker dariField;
    @FXML
    private DatePicker sampaiField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label totalPenjualan;
    @FXML
    private Label totalTiket;
    @FXML
    private Label jumlahBerhasil;
    @FXML
    private Label penjualanKiosk;
    @FXML
    private Label penjualanLoket;
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
    private TableColumn<BarisTransaksi, String> kolomStatus;
    @FXML
    private TableColumn<BarisTransaksi, String> kolomTotal;

    private final LaporanService laporanService = new LaporanService();

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdPemesanan())));
        kolomWaktu.setCellValueFactory(data -> new SimpleStringProperty(Waktu.format(data.getValue().getWaktu())));
        kolomFilm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJudulFilm()));
        kolomKanal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKanal().getLabel()));
        kolomStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().getLabel()));
        kolomTotal.setCellValueFactory(data -> new SimpleStringProperty(Rupiah.dari(data.getValue().getTotalBayar())));
        LocalDate hariIni = LocalDate.now();
        dariField.setValue(hariIni.withDayOfMonth(1));
        sampaiField.setValue(hariIni);
    }

    @Override
    public void siap(Admin admin) {
        terapkan();
    }

    @FXML
    private void terapkan() {
        errorLabel.setText("");
        try {
            RingkasanLaporan laporan = laporanService.laporan(dariField.getValue(), sampaiField.getValue());
            totalPenjualan.setText(Rupiah.dari(laporan.getTotalPenjualan()));
            totalTiket.setText(String.valueOf(laporan.getTotalTiket()));
            jumlahBerhasil.setText(String.valueOf(laporan.getJumlahTransaksiBerhasil()));
            penjualanKiosk.setText(Rupiah.dari(laporan.getPenjualanKiosk()));
            penjualanLoket.setText(Rupiah.dari(laporan.getPenjualanLoket()));
            tabel.getItems().setAll(laporan.getRekap());
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }
}
