package com.tiketin.controller.admin;

import com.tiketin.enums.StatusJadwal;
import com.tiketin.model.Admin;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Studio;
import com.tiketin.service.JadwalService;
import com.tiketin.util.Peringatan;
import com.tiketin.util.Rupiah;
import com.tiketin.util.Tabel;
import com.tiketin.util.Waktu;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JadwalController implements HalamanAdmin {
    private static final DateTimeFormatter JAM = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private TextField cariField;
    @FXML
    private TableView<JadwalTayang> tabel;
    @FXML
    private TableColumn<JadwalTayang, String> kolomId;
    @FXML
    private TableColumn<JadwalTayang, String> kolomFilm;
    @FXML
    private TableColumn<JadwalTayang, String> kolomStudio;
    @FXML
    private TableColumn<JadwalTayang, String> kolomWaktu;
    @FXML
    private TableColumn<JadwalTayang, String> kolomHarga;
    @FXML
    private TableColumn<JadwalTayang, String> kolomStatus;
    @FXML
    private Button tombolUbah;
    @FXML
    private Button tombolHapus;
    @FXML
    private VBox panelForm;
    @FXML
    private Label judulForm;
    @FXML
    private ComboBox<Film> filmField;
    @FXML
    private ComboBox<Studio> studioField;
    @FXML
    private DatePicker tanggalField;
    @FXML
    private ComboBox<LocalTime> jamField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<StatusJadwal> statusField;
    @FXML
    private Label errorForm;

    private final JadwalService jadwalService = new JadwalService();
    private Admin admin;
    private Integer idDiedit;

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdJadwal())));
        kolomFilm.setCellValueFactory(data -> new SimpleStringProperty(jadwalService.judul(data.getValue().getIdFilm())));
        kolomStudio.setCellValueFactory(data -> new SimpleStringProperty(jadwalService.namaStudio(data.getValue().getIdStudio())));
        kolomWaktu.setCellValueFactory(data -> new SimpleStringProperty(Waktu.format(data.getValue().getWaktuMulai())));
        kolomHarga.setCellValueFactory(data -> new SimpleStringProperty(Rupiah.dari(data.getValue().getHargaTiket())));
        Tabel.chip(kolomStatus, jadwal -> jadwal.getStatusJadwal().getLabel(),
                jadwal -> jadwal.getStatusJadwal() == StatusJadwal.AKTIF ? "aktif" : "batal");
        filmField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Film film) {
                return film == null ? "" : film.getJudul();
            }

            @Override
            public Film fromString(String string) {
                return null;
            }
        });
        studioField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Studio studio) {
                return studio == null ? "" : studio.getNamaStudio();
            }

            @Override
            public Studio fromString(String string) {
                return null;
            }
        });
        jamField.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalTime jam) {
                return jam == null ? "" : jam.format(JAM);
            }

            @Override
            public LocalTime fromString(String string) {
                return null;
            }
        });
        statusField.getItems().setAll(StatusJadwal.values());
        isiJam(null);
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
        filmField.getItems().setAll(jadwalService.film());
        studioField.getItems().setAll(jadwalService.studio());
        muat();
    }

    @FXML
    private void tampilkanFormBaru() {
        idDiedit = null;
        judulForm.setText("Tambah jadwal");
        filmField.getSelectionModel().clearSelection();
        studioField.getSelectionModel().clearSelection();
        tanggalField.setValue(null);
        isiJam(null);
        jamField.getSelectionModel().select(LocalTime.of(19, 0));
        hargaField.clear();
        statusField.getSelectionModel().select(StatusJadwal.AKTIF);
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void tampilkanFormUbah() {
        JadwalTayang jadwal = tabel.getSelectionModel().getSelectedItem();
        if (jadwal == null) {
            return;
        }
        idDiedit = jadwal.getIdJadwal();
        judulForm.setText("Ubah jadwal");
        filmField.getItems().stream().filter(film -> film.getIdFilm() == jadwal.getIdFilm()).findFirst()
                .ifPresent(filmField.getSelectionModel()::select);
        studioField.getItems().stream().filter(studio -> studio.getIdStudio() == jadwal.getIdStudio()).findFirst()
                .ifPresent(studioField.getSelectionModel()::select);
        tanggalField.setValue(jadwal.getWaktuMulai().toLocalDate());
        isiJam(jadwal.getWaktuMulai().toLocalTime());
        jamField.getSelectionModel().select(jadwal.getWaktuMulai().toLocalTime());
        hargaField.setText(String.valueOf(jadwal.getHargaTiket()));
        statusField.getSelectionModel().select(jadwal.getStatusJadwal());
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void simpan() {
        Film film = filmField.getValue();
        Studio studio = studioField.getValue();
        try {
            int idFilm = film == null ? 0 : film.getIdFilm();
            int idStudio = studio == null ? 0 : studio.getIdStudio();
            if (idDiedit == null) {
                jadwalService.tambah(idFilm, idStudio, tanggalField.getValue(), jamField.getValue(),
                        hargaField.getText(), statusField.getValue(), admin.getIdAdmin());
            } else {
                jadwalService.ubah(idDiedit, idFilm, idStudio, tanggalField.getValue(), jamField.getValue(),
                        hargaField.getText(), statusField.getValue());
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
        JadwalTayang jadwal = tabel.getSelectionModel().getSelectedItem();
        if (jadwal == null) {
            return;
        }
        if (!Peringatan.konfirmasi("Hapus jadwal " + jadwalService.judul(jadwal.getIdFilm()) + "?")) {
            return;
        }
        try {
            jadwalService.hapus(jadwal.getIdJadwal());
            muat();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Peringatan.gagal(ex.getMessage());
        }
    }

    private void muat() {
        tabel.getItems().setAll(jadwalService.cari(cariField.getText()));
    }

    private void isiJam(LocalTime tambahan) {
        List<LocalTime> jam = new ArrayList<>();
        for (int menit = 10 * 60; menit <= 22 * 60; menit += 30) {
            jam.add(LocalTime.of(menit / 60, menit % 60));
        }
        if (tambahan != null && !jam.contains(tambahan)) {
            jam.add(tambahan);
        }
        jamField.getItems().setAll(jam);
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
