package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.service.ProfilService;
import com.tiketin.util.Avatar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

public class ProfilController implements HalamanAdmin {
    @FXML
    private ImageView fotoBesar;
    @FXML
    private Label inisialBesar;
    @FXML
    private TextField namaField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label pesan;

    private final ProfilService profilService = new ProfilService();
    private Admin admin;
    private AdminShellController shell;
    private Path fotoBaru;
    private boolean hapusFoto;

    @Override
    public void siap(Admin admin) {
        this.admin = admin;
        this.fotoBaru = null;
        this.hapusFoto = false;
        namaField.setText(admin.getNama());
        usernameField.setText(admin.getUsername());
        pesan.setText("");
        tampilkan(admin.getFoto());
    }

    public void pasangShell(AdminShellController shell) {
        this.shell = shell;
    }

    @FXML
    private void pilihFoto() {
        FileChooser pemilih = new FileChooser();
        pemilih.setTitle("Pilih foto profil");
        pemilih.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar", "*.png", "*.jpg", "*.jpeg"));
        File berkas = pemilih.showOpenDialog(namaField.getScene().getWindow());
        if (berkas == null) {
            return;
        }
        fotoBaru = berkas.toPath();
        hapusFoto = false;
        pesan.setText("");
        tampilkan(fotoBaru.toString());
    }

    @FXML
    private void hapusFoto() {
        fotoBaru = null;
        hapusFoto = true;
        pesan.setText("");
        tampilkan(null);
    }

    @FXML
    private void simpan() {
        pesan.setText("");
        try {
            profilService.simpan(admin, namaField.getText(), usernameField.getText(), fotoBaru, hapusFoto);
            fotoBaru = null;
            hapusFoto = false;
            tampilkan(admin.getFoto());
            if (shell != null) {
                shell.segarkanProfil();
            }
            pesan.getStyleClass().remove("error");
            pesan.setText("Identitas tersimpan.");
        } catch (IllegalArgumentException ex) {
            if (!pesan.getStyleClass().contains("error")) {
                pesan.getStyleClass().add("error");
            }
            pesan.setText(ex.getMessage());
        }
    }

    private void tampilkan(String path) {
        Avatar.pasang(fotoBesar, inisialBesar, namaField.getText(), path, 96);
    }
}
