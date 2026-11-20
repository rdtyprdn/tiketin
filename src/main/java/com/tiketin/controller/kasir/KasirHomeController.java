package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.LocalDate;

import com.tiketin.App;
import com.tiketin.model.Kasir;
import com.tiketin.model.KasirRiwayatItem;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    private final KasirTransaksiService service = new KasirTransaksiService();

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);
        hitungStatistikDashboard();
    }

    @FXML
    private void mulaiTransaksi() throws IOException {
        KasirSession.reset();
        App.setRoot("fxml/kasir/KasirFilm");
    }

    @FXML
    private void lihatRiwayat() throws IOException {
        App.setRoot("fxml/kasir/KasirRiwayat");
    }

    // Statistik hari ini dari database, hanya transaksi kasir yang login
    private void hitungStatistikDashboard() {
        int totalTransaksi = 0;
        int totalTiket = 0;
        double totalPendapatan = 0;

        Kasir kasir = KasirSession.getKasirLogin();
        if (kasir != null) {
            try {
                for (KasirRiwayatItem transaksi : service.riwayat(kasir.getIdKasir())) {
                    boolean hariIni = transaksi.getTanggal().toLocalDate().equals(LocalDate.now());
                    if (hariIni && "Lunas".equals(transaksi.getStatus())) {
                        totalTransaksi++;
                        totalTiket += transaksi.getJumlahTiket();
                        totalPendapatan += transaksi.getTotal();
                    }
                }
            } catch (IllegalArgumentException ex) {
                Peringatan.gagal(ex.getMessage());
            }
        }

        lblTransaksiHariIni.setText(String.valueOf(totalTransaksi));
        lblTiketTerjual.setText(String.valueOf(totalTiket));
        lblTotalPendapatan.setText(KasirUtil.rupiah(totalPendapatan));
    }
}
