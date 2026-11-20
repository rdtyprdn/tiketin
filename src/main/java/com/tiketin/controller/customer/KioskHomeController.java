package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.util.Peringatan;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class KioskHomeController {

    @FXML
    private Button mulaiPesanButton;

    @FXML
    private void initialize() {
        // Jalan pintas petugas: tekan Esc di halaman awal kiosk untuk kembali ke login
        mulaiPesanButton.sceneProperty().addListener((obs, lama, scene) -> {
            if (scene != null) {
                pasangTombolEsc(scene);
            }
        });
    }

    private void pasangTombolEsc(Scene scene) {
        scene.setOnKeyPressed(event -> {
            // hanya aktif selama halaman awal kiosk yang tampil
            if (event.getCode() == KeyCode.ESCAPE && mulaiPesanButton.getScene() == scene) {
                event.consume();
                keLogin(scene);
            }
        });
    }

    private void keLogin(Scene scene) {
        if (!Peringatan.konfirmasi("Keluar dari mode kiosk dan kembali ke halaman login?")) {
            return;
        }
        try {
            scene.setOnKeyPressed(null);
            ((Stage) scene.getWindow()).setTitle("CineBook");
            App.setRoot("fxml/login");
        } catch (IOException e) {
            e.printStackTrace();
            Peringatan.gagal("Halaman login tidak bisa dibuka: " + e.getMessage());
        }
    }

    @FXML
    private void handleMulaiPesan() {
        try {
            App.setRoot("fxml/customer/movie-list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
