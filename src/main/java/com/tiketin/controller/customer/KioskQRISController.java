
package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class KioskQRISController {

    @FXML
    private Label lblTotalPembayaran;

    private int totalPembayaran;

    private String judulFilm;
    private String tanggal;
    private String jam;
    private String studio;

    private final String kursi = "D5, D6";
    private final int jumlahTiket = 2;

    public void setDataPesanan(
            int total,
            String judul,
            String tanggal,
            String jam,
            String studio) {

        this.totalPembayaran = total;
        this.judulFilm = judul;
        this.tanggal = tanggal;
        this.jam = jam;
        this.studio = studio;

        lblTotalPembayaran.setText(
            "Rp" + String.format("%,d", totalPembayaran).replace(',', '.')
        );
    }

    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/kiosk-order-summary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKonfirmasiPembayaran() {
        try {
            var lokasiFXML = getClass().getResource(
                "/com/tiketin/fxml/customer/kiosk-ticket.fxml"
            );

            System.out.println("LOKASI E-TICKET: " + lokasiFXML);

            if (lokasiFXML == null) {
                System.out.println(
                    "FILE KIOSK-TICKET.FXML TIDAK DITEMUKAN!"
                );
                return;
            }

            FXMLLoader loader = new FXMLLoader(lokasiFXML);
            Parent root = loader.load();

            KioskTicketController controller = loader.getController();

            controller.setDataTiket(
                "TK-20261120-001",
                judulFilm,
                tanggal,
                jam,
                studio,
                kursi,
                jumlahTiket
            );

            App.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
