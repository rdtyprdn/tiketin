package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.model.JadwalTayang;
import com.tiketin.service.KioskService;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.KioskSession;
import com.tiketin.util.Peringatan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class KioskQRISController {

    @FXML
    private Label lblTotalPembayaran;

    private final KioskService service = new KioskService();

    @FXML
    private void initialize() {
        lblTotalPembayaran.setText(KasirUtil.rupiah(KioskSession.getTotalBayar()));
    }

    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/kiosk-order-summary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simulasi QRIS: tombol ini dianggap pembayaran berhasil, lalu pesanan disimpan ke database
    @FXML
    private void handleKonfirmasiPembayaran(ActionEvent event) {
        JadwalTayang jadwal = KioskSession.getJadwalTerpilih();
        if (jadwal == null || KioskSession.getKursiTerpilih().isEmpty()) {
            Peringatan.gagal("Pesanan belum lengkap. Silakan pilih jadwal dan kursi dulu.");
            return;
        }

        Node tombol = (Node) event.getSource();
        tombol.setDisable(true); // cegah klik dua kali
        try {
            int idPemesanan = service.bayarQris(jadwal, KioskSession.getKursiTerpilih());
            KioskSession.setIdPemesananTerakhir(idPemesanan);
            App.setRoot("fxml/customer/kiosk-ticket");
        } catch (IllegalArgumentException ex) {
            // contoh: kursi keburu dibeli orang lain di kasir
            Peringatan.gagal(ex.getMessage());
            tombol.setDisable(false);
            try {
                App.setRoot("fxml/customer/kiosk-seat-selection");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
