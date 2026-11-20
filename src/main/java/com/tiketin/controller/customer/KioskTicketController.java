
package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KioskTicketController {

    @FXML
    private Label lblKodeTiket;

    @FXML
    private Label lblJudulFilmTiket;

    @FXML
    private Label lblTanggalTiket;

    @FXML
    private Label lblJamTiket;

    @FXML
    private Label lblStudioTiket;

    @FXML
    private Label lblKursiTiket;

    @FXML
    private Label lblJumlahTiket;

    public void setDataTiket(
            String kode,
            String judul,
            String tanggal,
            String jam,
            String studio,
            String kursi,
            int jumlah) {

        lblKodeTiket.setText(kode);
        lblJudulFilmTiket.setText(judul);
        lblTanggalTiket.setText(tanggal);
        lblJamTiket.setText(jam);
        lblStudioTiket.setText(studio);
        lblKursiTiket.setText(kursi);
        lblJumlahTiket.setText(String.valueOf(jumlah));
    }

    @FXML
    private void handleSelesai() {
        try {
            App.setRoot("fxml/customer/kiosk-home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
