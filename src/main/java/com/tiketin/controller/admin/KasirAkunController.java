package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.AkunKasir;
import com.tiketin.service.KasirService;
import com.tiketin.util.Peringatan;
import com.tiketin.util.Tabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class KasirAkunController implements HalamanAdmin {
    @FXML
    private TextField cariField;
    @FXML
    private TableView<AkunKasir> tabel;
    @FXML
    private TableColumn<AkunKasir, String> kolomId;
    @FXML
    private TableColumn<AkunKasir, String> kolomNama;
    @FXML
    private TableColumn<AkunKasir, String> kolomUsername;
    @FXML
    private Button tombolUbah;
    @FXML
    private Button tombolHapus;
    @FXML
    private VBox panelForm;
    @FXML
    private Label judulForm;
    @FXML
    private TextField namaField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label bantuanPassword;
    @FXML
    private Label errorForm;

    private final KasirService kasirService = new KasirService();
    private Integer idDiedit;

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdKasir())));
        kolomNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));
        kolomUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        cariField.textProperty().addListener((obs, lama, baru) -> muat());
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            boolean ada = baru != null;
            tombolUbah.setDisable(!ada);
            tombolHapus.setDisable(!ada);
        });
        tombolUbah.setDisable(true);
        tombolHapus.setDisable(true);
        sembunyikanForm();
    }

    @Override
    public void siap(Admin admin) {
        muat();
    }

    @FXML
    private void tampilkanFormBaru() {
        idDiedit = null;
        judulForm.setText("Tambah kasir");
        namaField.clear();
        usernameField.clear();
        passwordField.clear();
        bantuanPassword.setText("Minimal 6 karakter.");
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void tampilkanFormUbah() {
        AkunKasir akun = tabel.getSelectionModel().getSelectedItem();
        if (akun == null) {
            return;
        }
        idDiedit = akun.getIdKasir();
        judulForm.setText("Ubah kasir");
        namaField.setText(akun.getNama());
        usernameField.setText(akun.getUsername());
        passwordField.clear();
        bantuanPassword.setText("Kosongkan jika password tidak diubah.");
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void simpan() {
        try {
            if (idDiedit == null) {
                kasirService.tambah(namaField.getText(), usernameField.getText(), passwordField.getText());
            } else {
                kasirService.ubah(idDiedit, namaField.getText(), usernameField.getText(), passwordField.getText());
            }
            sembunyikanForm();
            muat();
        } catch (IllegalArgumentException ex) {
            errorForm.setText(ex.getMessage());
        }
    }

    @FXML
    private void batal() {
        sembunyikanForm();
    }

    @FXML
    private void hapus() {
        AkunKasir akun = tabel.getSelectionModel().getSelectedItem();
        if (akun == null) {
            return;
        }
        if (!Peringatan.konfirmasi("Hapus akun " + akun.getNama() + "?")) {
            return;
        }
        try {
            kasirService.hapus(akun.getIdKasir());
            muat();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Peringatan.gagal(ex.getMessage());
        }
    }

    private void muat() {
        tabel.getItems().setAll(kasirService.cari(cariField.getText()));
    }

    private void tampilkanForm() {
        panelForm.setVisible(true);
        panelForm.setManaged(true);
    }

    private void sembunyikanForm() {
        panelForm.setVisible(false);
        panelForm.setManaged(false);
    }
}
