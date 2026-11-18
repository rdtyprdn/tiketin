package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.Kursi;
import com.tiketin.model.Studio;
import com.tiketin.service.KursiService;
import com.tiketin.service.StudioService;
import com.tiketin.util.Peringatan;
import com.tiketin.util.Tabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class KursiController implements HalamanAdmin {
    @FXML
    private ComboBox<Studio> filterStudio;
    @FXML
    private TextField cariField;
    @FXML
    private TableView<Kursi> tabel;
    @FXML
    private TableColumn<Kursi, String> kolomId;
    @FXML
    private TableColumn<Kursi, String> kolomStudio;
    @FXML
    private TableColumn<Kursi, String> kolomNomor;
    @FXML
    private TableColumn<Kursi, String> kolomTipe;
    @FXML
    private Button tombolUbah;
    @FXML
    private Button tombolHapus;
    @FXML
    private VBox panelForm;
    @FXML
    private Label judulForm;
    @FXML
    private ComboBox<Studio> studioField;
    @FXML
    private TextField nomorField;
    @FXML
    private ComboBox<String> tipeField;
    @FXML
    private Label errorForm;

    private final KursiService kursiService = new KursiService();
    private final StudioService studioService = new StudioService();
    private Admin admin;
    private Integer idDiedit;

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdKursi())));
        kolomStudio.setCellValueFactory(data -> new SimpleStringProperty(studioService.nama(data.getValue().getIdStudio())));
        kolomNomor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomorKursi()));
        kolomTipe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipeKursi()));
        pasangTeksStudio(filterStudio);
        pasangTeksStudio(studioField);
        tipeField.getItems().setAll("Reguler", "VIP");
        filterStudio.valueProperty().addListener((obs, lama, baru) -> muat());
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
        this.admin = admin;
        filterStudio.getItems().setAll(studioService.cari(""));
        filterStudio.getSelectionModel().clearSelection();
        studioField.getItems().setAll(studioService.cari(""));
        muat();
    }

    @FXML
    private void tampilkanFormBaru() {
        idDiedit = null;
        judulForm.setText("Tambah kursi");
        studioField.getSelectionModel().clearSelection();
        nomorField.clear();
        tipeField.getSelectionModel().select("Reguler");
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void tampilkanFormUbah() {
        Kursi kursi = tabel.getSelectionModel().getSelectedItem();
        if (kursi == null) {
            return;
        }
        idDiedit = kursi.getIdKursi();
        judulForm.setText("Ubah kursi");
        studioField.getItems().stream()
                .filter(studio -> studio.getIdStudio() == kursi.getIdStudio())
                .findFirst()
                .ifPresent(studioField.getSelectionModel()::select);
        nomorField.setText(kursi.getNomorKursi());
        tipeField.getSelectionModel().select(kursi.getTipeKursi());
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void simpan() {
        Studio studio = studioField.getValue();
        try {
            int idStudio = studio == null ? 0 : studio.getIdStudio();
            if (idDiedit == null) {
                kursiService.tambah(idStudio, nomorField.getText(), tipeField.getValue(), admin.getIdAdmin());
            } else {
                kursiService.ubah(idDiedit, idStudio, nomorField.getText(), tipeField.getValue());
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
        Kursi kursi = tabel.getSelectionModel().getSelectedItem();
        if (kursi == null) {
            return;
        }
        if (!Peringatan.konfirmasi("Hapus kursi " + kursi.getNomorKursi() + "?")) {
            return;
        }
        try {
            kursiService.hapus(kursi.getIdKursi());
            muat();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Peringatan.gagal(ex.getMessage());
        }
    }

    private void muat() {
        Studio filter = filterStudio.getValue();
        Integer idStudio = filter == null ? null : filter.getIdStudio();
        tabel.getItems().setAll(kursiService.cari(idStudio, cariField.getText()));
    }

    private void pasangTeksStudio(ComboBox<Studio> combo) {
        combo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Studio studio) {
                return studio == null ? "" : studio.getNamaStudio();
            }

            @Override
            public Studio fromString(String string) {
                return null;
            }
        });
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
