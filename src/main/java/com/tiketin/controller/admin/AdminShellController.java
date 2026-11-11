package com.tiketin.controller.admin;

import java.io.IOException;
import java.util.List;

import com.tiketin.App;
import com.tiketin.model.Admin;
import com.tiketin.util.Avatar;
import com.tiketin.util.Peringatan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminShellController {
    @FXML
    private Label namaAdmin;
    @FXML
    private Label usernameAdmin;
    @FXML
    private Label inisialAdmin;
    @FXML
    private ImageView fotoAdmin;
    @FXML
    private Label namaKepala;
    @FXML
    private Label inisialKepala;
    @FXML
    private ImageView fotoKepala;
    @FXML
    private Label judulHalaman;
    @FXML
    private StackPane konten;
    @FXML
    private Button navDashboard;
    @FXML
    private Button navFilm;
    @FXML
    private Button navStudio;
    @FXML
    private Button navKursi;
    @FXML
    private Button navJadwal;
    @FXML
    private Button navKasir;
    @FXML
    private Button navTransaksi;
    @FXML
    private Button navLaporan;
    @FXML
    private Button navProfil;

    private Admin admin;

    public void siap(Admin admin) {
        this.admin = admin;
        segarkanProfil();
        bukaDashboard();
    }

    public void segarkanProfil() {
        namaAdmin.setText(admin.getNama());
        usernameAdmin.setText(admin.getUsername());
        namaKepala.setText(admin.getNama());
        Avatar.pasang(fotoAdmin, inisialAdmin, admin.getNama(), admin.getFoto(), 40);
        Avatar.pasang(fotoKepala, inisialKepala, admin.getNama(), admin.getFoto(), 36);
    }

    @FXML
    private void bukaDashboard() {
        buka("dashboard.fxml", "Dashboard", navDashboard);
    }

    @FXML
    private void bukaFilm() {
        buka("film.fxml", "Film", navFilm);
    }

    @FXML
    private void bukaStudio() {
        buka("studio.fxml", "Studio", navStudio);
    }

    @FXML
    private void bukaKursi() {
        buka("kursi.fxml", "Kursi", navKursi);
    }

    @FXML
    private void bukaJadwal() {
        buka("jadwal.fxml", "Jadwal tayang", navJadwal);
    }

    @FXML
    private void bukaKasir() {
        buka("kasir.fxml", "Akun kasir", navKasir);
    }

    @FXML
    private void bukaTransaksi() {
        buka("transaksi.fxml", "Transaksi", navTransaksi);
    }

    @FXML
    private void bukaLaporan() {
        buka("laporan.fxml", "Laporan penjualan", navLaporan);
    }

    @FXML
    private void bukaProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/tiketin/fxml/admin/profil.fxml"));
            Parent view = loader.load();
            ProfilController halaman = loader.getController();
            halaman.pasangShell(this);
            halaman.siap(admin);
            konten.getChildren().setAll(view);
            judulHalaman.setText("Profil");
            tandai(navProfil);
        } catch (IOException ex) {
            Peringatan.gagal("Halaman profil tidak bisa dibuka.");
        }
    }

    @FXML
    private void keluar() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/tiketin/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) konten.getScene().getWindow();
            stage.setTitle("Tiketin");
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            Peringatan.gagal("Halaman login tidak bisa dibuka.");
        }
    }

    private void buka(String file, String judul, Button aktif) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/tiketin/fxml/admin/" + file));
            Parent view = loader.load();
            Object controller = loader.getController();
            if (controller instanceof HalamanAdmin halaman) {
                halaman.siap(admin);
            }
            konten.getChildren().setAll(view);
            judulHalaman.setText(judul);
            tandai(aktif);
        } catch (IOException ex) {
            Peringatan.gagal("Halaman tidak bisa dibuka.");
        }
    }

    private void tandai(Button aktif) {
        for (Button tombol : List.of(navDashboard, navFilm, navStudio, navKursi, navJadwal, navKasir, navTransaksi, navLaporan, navProfil)) {
            tombol.getStyleClass().remove("aktif");
        }
        aktif.getStyleClass().add("aktif");
    }
}
