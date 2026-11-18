package com.tiketin.controller;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.controller.admin.AdminShellController;
import com.tiketin.model.Admin;
import com.tiketin.model.Pengguna;
import com.tiketin.service.AuthService;

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
        errorLabel.setText("");
        try {
            Pengguna pengguna = authService.login(usernameField.getText(), passwordField.getText());
            if ("ADMIN".equals(pengguna.peran())) {
                bukaAdmin((Admin) pengguna);
            } else if ("KASIR".equals(pengguna.peran())) {
                errorLabel.setText("Akun kasir dikenali. Dashboard kasir belum dipasang di bagian ini.");
            } else {
                errorLabel.setText("Peran akun tidak dikenali.");
            }
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        } catch (IOException ex) {
            errorLabel.setText("Halaman admin tidak bisa dibuka.");
        }
    }

    private void bukaAdmin(Admin admin) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/tiketin/fxml/admin/shell.fxml"));
        Parent root = loader.load();
        loader.<AdminShellController>getController().siap(admin);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Tiketin Admin");
        stage.getScene().setRoot(root);
    }
}
