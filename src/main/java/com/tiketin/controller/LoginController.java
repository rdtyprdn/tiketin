package com.tiketin.controller;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.controller.admin.AdminShellController;
import com.tiketin.model.Admin;
import com.tiketin.model.Kasir;
import com.tiketin.model.Pengguna;
import com.tiketin.service.AuthService;
import com.tiketin.service.ProfilService;
import com.tiketin.util.KasirSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void masuk() {
        tampilkanGalat("");
        try {
            Pengguna pengguna = authService.login(usernameField.getText(), passwordField.getText());
            if ("ADMIN".equals(pengguna.getPeran())) {
                bukaAdmin((Admin) pengguna);
            } else if ("KASIR".equals(pengguna.getPeran())) {
                bukaKasir((Kasir) pengguna);
            } else {
                tampilkanGalat("Peran akun tidak dikenali.");
            }
        } catch (IllegalArgumentException ex) {
            tampilkanGalat(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            tampilkanGalat("Halaman tidak bisa dibuka: " + ex.getMessage());
        }
    }

    private void tampilkanGalat(String pesan) {
        boolean ada = pesan != null && !pesan.isBlank();
        errorLabel.setText(ada ? pesan : "");
        errorLabel.setVisible(ada);
        errorLabel.setManaged(ada);
    }

    private void bukaAdmin(Admin admin) throws IOException {
        ProfilService.terapkanTersimpan(admin);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/tiketin/fxml/admin/shell.fxml"));
        Parent root = loader.load();
        loader.<AdminShellController>getController().siap(admin);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Tiketin Admin");
        stage.getScene().setRoot(root);
    }

    // Buka halaman kiosk (pelanggan) tanpa login
    @FXML
    private void bukaKiosk() {
        tampilkanGalat("");
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Tiketin Kiosk");
            App.setRoot("fxml/customer/kiosk-home");
        } catch (IOException ex) {
            ex.printStackTrace();
            tampilkanGalat("Halaman kiosk tidak bisa dibuka: " + ex.getMessage());
        }
    }

    private void bukaKasir(Kasir kasir) throws IOException {
        KasirSession.setKasirLogin(kasir);
        KasirSession.reset();
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Tiketin Kasir");
        App.setRoot("fxml/kasir/KasirHome");
    }
}