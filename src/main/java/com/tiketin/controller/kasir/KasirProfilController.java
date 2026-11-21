package com.tiketin.controller.kasir;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.model.Kasir;
import com.tiketin.service.KasirService;
import com.tiketin.util.Avatar;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.PasswordUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

// Halaman Profil Kasir: lihat identitas, ubah nama, ubah password (disimpan ke tabel pengguna)
public class KasirProfilController {

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    @FXML
    private Label lblInisial;

    @FXML
    private Label lblNamaBesar;

    @FXML
    private Label lblUsernameKecil;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblPesanNama;

    @FXML
    private PasswordField txtPasswordLama;

    @FXML
    private PasswordField txtPasswordBaru;

    @FXML
    private PasswordField txtPasswordUlang;

    @FXML
    private Label lblPesanPassword;

    private final KasirService service = new KasirService();

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);
        tampilkan(KasirSession.getKasirLogin());
    }

    private void tampilkan(Kasir kasir) {
        if (kasir == null) {
            return;
        }
        lblInisial.setText(Avatar.inisial(kasir.getNama()));
        lblNamaBesar.setText(kasir.getNama());
        lblUsernameKecil.setText("@" + kasir.getUsername());
        lblIdKasir.setText(kasir.getNama());
        txtNama.setText(kasir.getNama());
        txtUsername.setText(kasir.getUsername());
    }

    @FXML
    private void simpanNama() {
        Kasir kasir = KasirSession.getKasirLogin();
        if (kasir == null) {
            pesan(lblPesanNama, "Sesi kasir tidak ditemukan. Silakan masuk ulang.", false);
            return;
        }
        try {
            // username tetap, password tidak diubah (null)
            Kasir baru = service.ubah(kasir.getIdKasir(), txtNama.getText(), kasir.getUsername(), null);
            KasirSession.setKasirLogin(baru);
            tampilkan(baru);
            pesan(lblPesanNama, "Nama berhasil disimpan.", true);
        } catch (IllegalArgumentException ex) {
            pesan(lblPesanNama, ex.getMessage(), false);
        }
    }

    @FXML
    private void ubahPassword() {
        Kasir kasir = KasirSession.getKasirLogin();
        if (kasir == null) {
            pesan(lblPesanPassword, "Sesi kasir tidak ditemukan. Silakan masuk ulang.", false);
            return;
        }
        String lama = txtPasswordLama.getText();
        String baru = txtPasswordBaru.getText();
        String ulang = txtPasswordUlang.getText();
        if (lama.isBlank() || baru.isBlank() || ulang.isBlank()) {
            pesan(lblPesanPassword, "Isi password lama, password baru, dan ulangi password baru.", false);
            return;
        }
        if (!PasswordUtil.cocok(lama, kasir.getPassword())) {
            pesan(lblPesanPassword, "Password lama salah.", false);
            return;
        }
        if (!baru.equals(ulang)) {
            pesan(lblPesanPassword, "Password baru dan ulangannya tidak sama.", false);
            return;
        }
        try {
            // KasirService.ubah memvalidasi minimal 6 karakter dan meng-hash password
            Kasir hasil = service.ubah(kasir.getIdKasir(), kasir.getNama(), kasir.getUsername(), baru);
            KasirSession.setKasirLogin(hasil);
            txtPasswordLama.clear();
            txtPasswordBaru.clear();
            txtPasswordUlang.clear();
            pesan(lblPesanPassword, "Password berhasil diubah.", true);
        } catch (IllegalArgumentException ex) {
            pesan(lblPesanPassword, ex.getMessage(), false);
        }
    }

    private void pesan(Label label, String teks, boolean berhasil) {
        label.setText(teks);
        label.setStyle(berhasil ? "-fx-text-fill: #16a34a;" : "-fx-text-fill: #dc2626;");
    }

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirHome");
    }
}
