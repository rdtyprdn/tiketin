package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.Film;
import com.tiketin.service.FilmService;
import com.tiketin.util.Peringatan;
import com.tiketin.util.PosterUtil;
import com.tiketin.util.Tabel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

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
    private TextField sutradaraField;
    @FXML
    private TextField tahunField;
    @FXML
    private ComboBox<String> ratingUsiaField;
    @FXML
    private TextField ratingField;
    @FXML
    private Label detailInfo;
    @FXML
    private Label errorForm;
    @FXML
    private Label inisialLabel;
    @FXML
    private StackPane kotakPoster;
    @FXML
    private ImageView gambarPoster;
    @FXML
    private StackPane kotakPreview;
    @FXML
    private ImageView previewPoster;
    @FXML
    private Label previewKosong;
    @FXML
    private Label infoPoster;
    @FXML
    private Button tombolHapusPoster;
    @FXML
    private Label detailJudul;
    @FXML
    private Label detailMeta;
    @FXML
    private Label detailSinopsis;

    private final FilmService filmService = new FilmService();
    private Admin admin;
    private Integer idDiedit;
    private Path posterDipilih;      // file baru dari "Pilih poster" (belum disimpan)
    private boolean posterDihapus;   // "Hapus poster" ditekan
    private static File folderTerakhir;

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
        ratingUsiaField.getItems().setAll(FilmService.RATING_USIA);
        ratingUsiaField.getSelectionModel().select("SU");
        aturKotakPoster();
        sembunyikanForm();
        isiDetail(null);
    }

    // Poster detail selalu muat di dalam kotak navy: ukuran gambar mengikuti kotak, sudut dipotong bulat
    private void aturKotakPoster() {
        gambarPoster.fitWidthProperty().bind(kotakPoster.widthProperty().subtract(16));
        gambarPoster.fitHeightProperty().bind(kotakPoster.heightProperty().subtract(16));
        Rectangle klip = new Rectangle();
        klip.setArcWidth(24);
        klip.setArcHeight(24);
        klip.widthProperty().bind(kotakPoster.widthProperty());
        klip.heightProperty().bind(kotakPoster.heightProperty());
        kotakPoster.setClip(klip);

        Rectangle klipMini = new Rectangle(60, 90);
        klipMini.setArcWidth(16);
        klipMini.setArcHeight(16);
        kotakPreview.setClip(klipMini);
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
        sutradaraField.clear();
        tahunField.clear();
        ratingUsiaField.getSelectionModel().select("SU");
        ratingField.clear();
        errorForm.setText("");
        resetPosterForm(null);
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
        sutradaraField.setText(film.getSutradara() == null ? "" : film.getSutradara());
        tahunField.setText(film.getTahunRilis() == null ? "" : String.valueOf(film.getTahunRilis()));
        ratingUsiaField.getSelectionModel().select(film.getRatingUsia() == null ? "SU" : film.getRatingUsia());
        ratingField.setText(film.getRating() == null ? "" : FilmService.formatRating(film.getRating()));
        errorForm.setText("");
        resetPosterForm(film);
        tampilkanForm();
    }

    @FXML
    private void simpan() {
        try {
            FilmService.Detail detail = new FilmService.Detail(sutradaraField.getText(), tahunField.getText(),
                    ratingUsiaField.getValue(), ratingField.getText());
            if (idDiedit == null) {
                filmService.tambah(judulField.getText(), genreField.getText(), durasiField.getText(),
                        sinopsisField.getText(), detail, admin.getIdAdmin(), posterDipilih);
            } else {
                filmService.ubah(idDiedit, judulField.getText(), genreField.getText(), durasiField.getText(),
                        sinopsisField.getText(), detail, posterDipilih, posterDihapus);
            }
            sembunyikanForm();
            muat();
        } catch (IllegalArgumentException ex) {
            errorForm.setText(ex.getMessage());
        }
    }

    @FXML
    private void pilihPoster() {
        FileChooser pilih = new FileChooser();
        pilih.setTitle("Pilih poster film");
        pilih.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar (JPG, JPEG, PNG)",
                "*.jpg", "*.jpeg", "*.png", "*.JPG", "*.JPEG", "*.PNG"));
        if (folderTerakhir != null && folderTerakhir.isDirectory()) {
            pilih.setInitialDirectory(folderTerakhir);
        }
        File file = pilih.showOpenDialog(panelForm.getScene().getWindow());
        if (file != null) {
            folderTerakhir = file.getParentFile();
            pakaiPoster(file.toPath());
        }
    }

    // Dipanggil setelah file dipilih: cek file, tampilkan pratinjau (belum disimpan sampai Simpan)
    private void pakaiPoster(Path file) {
        try {
            Image gambar = PosterUtil.bacaFile(file);
            posterDipilih = file;
            posterDihapus = false;
            tampilkanPreview(gambar);
            infoPoster.setText(file.getFileName().toString());
            errorForm.setText("");
        } catch (IllegalArgumentException ex) {
            errorForm.setText(ex.getMessage());
        }
    }

    @FXML
    private void hapusPosterForm() {
        posterDipilih = null;
        posterDihapus = true;
        tampilkanPreview(null);
        infoPoster.setText("Poster akan dihapus saat Simpan.");
    }

    private void resetPosterForm(Film film) {
        posterDipilih = null;
        posterDihapus = false;
        Image ada = film == null ? null : PosterUtil.cari(film);
        tampilkanPreview(ada);
        infoPoster.setText(ada == null ? "Belum ada poster. JPG / PNG, maks 5 MB" : "Poster saat ini. JPG / PNG, maks 5 MB");
    }

    private void tampilkanPreview(Image gambar) {
        previewPoster.setImage(gambar);
        previewKosong.setVisible(gambar == null);
        tombolHapusPoster.setDisable(gambar == null);
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
            gambarPoster.setImage(null);
            inisialLabel.setVisible(true);
            inisialLabel.setText("?");
            detailJudul.setText("Pilih film");
            detailMeta.setText("Detail muncul di sini.");
            detailInfo.setText("");
            detailSinopsis.setText("");
            return;
        }
        // Kalau ada file poster sesuai judul, tampilkan; kalau tidak, tetap huruf awal judul
        Image poster = PosterUtil.cari(film);
        gambarPoster.setImage(poster);
        inisialLabel.setVisible(poster == null);
        inisialLabel.setText(poster == null ? film.getJudul().substring(0, 1).toUpperCase() : "");
        detailJudul.setText(film.getJudul());
        detailMeta.setText(film.getGenre() + " · " + film.getDurasi() + " menit");
        detailInfo.setText("Sutradara: " + (film.getSutradara() == null ? "-" : film.getSutradara())
                + "\nTahun " + (film.getTahunRilis() == null ? "-" : film.getTahunRilis())
                + " · " + (film.getRatingUsia() == null ? "SU" : film.getRatingUsia())
                + " · ★ " + FilmService.formatRating(film.getRating()));
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
