package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.Film;
import com.tiketin.service.FilmService;
import com.tiketin.util.Peringatan;
import com.tiketin.util.Tabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FilmController implements HalamanAdmin {
    @FXML
    private TextField cariField;
    @FXML
    private TableView<Film> tabel;
    @FXML
    private TableColumn<Film, String> kolomId;
    @FXML
    private TableColumn<Film, String> kolomJudul;
    @FXML
    private TableColumn<Film, String> kolomGenre;
    @FXML
    private TableColumn<Film, String> kolomDurasi;
    @FXML
    private Button tombolUbah;
    @FXML
    private Button tombolHapus;
    @FXML
    private VBox panelForm;
    @FXML
    private Label judulForm;
    @FXML
    private TextField judulField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField durasiField;
    @FXML
    private TextArea sinopsisField;
    @FXML
    private Label errorForm;
    @FXML
    private Label inisialLabel;
    @FXML
    private Label detailJudul;
    @FXML
    private Label detailMeta;
    @FXML
    private Label detailSinopsis;

    private final FilmService filmService = new FilmService();
    private Admin admin;
    private Integer idDiedit;

    @FXML
    private void initialize() {
        Tabel.penuh(tabel);
        kolomId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdFilm())));
        kolomJudul.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJudul()));
        kolomGenre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre()));
        kolomDurasi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDurasi() + " menit"));
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            boolean ada = baru != null;
            tombolUbah.setDisable(!ada);
            tombolHapus.setDisable(!ada);
            isiDetail(baru);
        });
        cariField.textProperty().addListener((obs, lama, baru) -> muat());
        tombolUbah.setDisable(true);
        tombolHapus.setDisable(true);
        sembunyikanForm();
        isiDetail(null);
    }

    @Override
    public void siap(Admin admin) {
        this.admin = admin;
        muat();
    }

    @FXML
    private void tampilkanFormBaru() {
        idDiedit = null;
        judulForm.setText("Tambah film");
        judulField.clear();
        genreField.clear();
        durasiField.clear();
        sinopsisField.clear();
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void tampilkanFormUbah() {
        Film film = tabel.getSelectionModel().getSelectedItem();
        if (film == null) {
            return;
        }
        idDiedit = film.getIdFilm();
        judulForm.setText("Ubah film");
        judulField.setText(film.getJudul());
        genreField.setText(film.getGenre());
        durasiField.setText(String.valueOf(film.getDurasi()));
        sinopsisField.setText(film.getSinopsis());
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void simpan() {
        try {
            if (idDiedit == null) {
                filmService.tambah(judulField.getText(), genreField.getText(), durasiField.getText(),
                        sinopsisField.getText(), admin.getIdAdmin());
            } else {
                filmService.ubah(idDiedit, judulField.getText(), genreField.getText(), durasiField.getText(),
                        sinopsisField.getText());
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
        Film film = tabel.getSelectionModel().getSelectedItem();
        if (film == null) {
            return;
        }
        if (!Peringatan.konfirmasi("Hapus film " + film.getJudul() + "?")) {
            return;
        }
        try {
            filmService.hapus(film.getIdFilm());
            muat();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Peringatan.gagal(ex.getMessage());
        }
    }

    private void muat() {
        tabel.getItems().setAll(filmService.cari(cariField.getText()));
        isiDetail(tabel.getSelectionModel().getSelectedItem());
    }

    private void isiDetail(Film film) {
        if (film == null) {
            inisialLabel.setText("?");
            detailJudul.setText("Pilih film");
            detailMeta.setText("Detail muncul di sini.");
            detailSinopsis.setText("");
            return;
        }
        inisialLabel.setText(film.getJudul().substring(0, 1).toUpperCase());
        detailJudul.setText(film.getJudul());
        detailMeta.setText(film.getGenre() + " · " + film.getDurasi() + " menit");
        detailSinopsis.setText(film.getSinopsis());
    }

    private void tampilkanForm() {
        panelForm.setVisible(true);
        panelForm.setManaged(true);
    }

    private void sembunyikanForm() {
        panelForm.setVisible(false);
        panelForm.setManaged(false);
        errorForm.setText("");
    }
}
