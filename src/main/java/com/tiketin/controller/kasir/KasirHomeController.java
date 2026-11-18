package com.tiketin.controller.kasir;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.tiketin.model.KasirRiwayatItem;
import com.tiketin.util.KasirRiwayatData;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.Locale;

public class KasirHomeController {

    @FXML
    private Button btnMulaiTransaksi;

    @FXML
    private Button btnLihatRiwayat;

    @FXML
    private Button btnKeluar;

    @FXML
    private Label lblIdKasir;

    @FXML
    private Label lblTransaksiHariIni;

    @FXML
    private Label lblTiketTerjual;

    @FXML
    private Label lblTotalPendapatan;

    @FXML
    private void mulaiTransaksi() throws IOException {
    App.setRoot("fxml/kasir/KasirFilm");
    }

    @FXML
    private void lihatRiwayat() throws IOException {
    App.setRoot("fxml/kasir/KasirRiwayat");
    }

    @FXML
    private void initialize() {
        hitungStatistikDashboard();
    }
    
    private void hitungStatistikDashboard() {
    
        int totalTransaksi = 0;
        int totalTiket = 0;
        int totalPendapatan = 0;
    
        for (KasirRiwayatItem transaksi :
                KasirRiwayatData.getDaftarTransaksi()) {
    
            if (transaksi.getTanggal().toLocalDate()
                    .equals(LocalDate.now())) {
    
                totalTransaksi++;
                totalTiket += transaksi.getJumlahTiket();
                totalPendapatan += transaksi.getTotal();
            }
        }
    
        NumberFormat format = NumberFormat.getNumberInstance(
            Locale.forLanguageTag("id-ID")
        );
    
        lblTransaksiHariIni.setText(String.valueOf(totalTransaksi));
        lblTiketTerjual.setText(String.valueOf(totalTiket));
        lblTotalPendapatan.setText("Rp" + format.format(totalPendapatan));
    }
}