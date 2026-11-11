package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.Studio;
import com.tiketin.service.StudioService;
import com.tiketin.util.Peringatan;
import com.tiketin.util.Tabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class StudioController implements HalamanAdmin {
    @FXML
    private TextField cariField;
    @FXML
    private TableView<Studio> tabel;
    @FXML
    private TableColumn<Studio, String> kolomId;
    @FXML
    private TableColumn<Studio, String> kolomNama;
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
    private Label errorForm;

    private final StudioService studioService = new StudioService();
    private Admin admin;
    private Integer idDiedit;

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdStudio())));
        kolomNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaStudio()));
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            boolean ada = baru != null;
            tombolUbah.setDisable(!ada);
            tombolHapus.setDisable(!ada);
        });
        cariField.textProperty().addListener((obs, lama, baru) -> muat());
        tombolUbah.setDisable(true);
        tombolHapus.setDisable(true);
        sembunyikanForm();
    }

    @Override
    public void siap(Admin admin) {
        this.admin = admin;
        muat();
    }

    @FXML
    private void tampilkanFormBaru() {
        idDiedit = null;
        judulForm.setText("Tambah studio");
        namaField.clear();
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void tampilkanFormUbah() {
        Studio studio = tabel.getSelectionModel().getSelectedItem();
        if (studio == null) {
            return;
        }
        idDiedit = studio.getIdStudio();
        judulForm.setText("Ubah studio");
        namaField.setText(studio.getNamaStudio());
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void simpan() {
        try {
            if (idDiedit == null) {
                studioService.tambah(namaField.getText(), admin.getIdAdmin());
            } else {
                studioService.ubah(idDiedit, namaField.getText());
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
        Studio studio = tabel.getSelectionModel().getSelectedItem();
        if (studio == null) {
            return;
        }
        if (!Peringatan.konfirmasi("Hapus " + studio.getNamaStudio() + "?")) {
            return;
        }
        try {
            studioService.hapus(studio.getIdStudio());
            muat();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Peringatan.gagal(ex.getMessage());
        }
    }

    private void muat() {
        tabel.getItems().setAll(studioService.cari(cariField.getText()));
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
