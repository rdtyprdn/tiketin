package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.Kursi;
import com.tiketin.model.Studio;
import com.tiketin.service.KursiService;
import com.tiketin.service.StudioService;
import com.tiketin.util.Peringatan;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KursiController implements HalamanAdmin {
    private static final String GAYA_REGULER = "-fx-background-color: white; -fx-text-fill: #1A2C50; -fx-border-color: #1A2C50; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: 700; -fx-cursor: hand;";
    private static final String GAYA_VIP = "-fx-background-color: #FBC436; -fx-text-fill: #1A2C50; -fx-border-color: #1A2C50; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: 700; -fx-cursor: hand;";
    private static final String GAYA_PILIH = "-fx-background-color: #0F8EED; -fx-text-fill: white; -fx-border-color: #0F8EED; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: 700; -fx-cursor: hand;";

    @FXML
    private ComboBox<Studio> filterStudio;
    @FXML
    private TextField cariField;
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
    @FXML
    private VBox wadahDenah;

    private final KursiService kursiService = new KursiService();
    private final StudioService studioService = new StudioService();
    private Admin admin;
    private Integer idDiedit;
    private Kursi dipilih;
    private List<Kursi> tampilan = List.of();

    @FXML
    private void initialize() {
        pasangTeksStudio(filterStudio);
        pasangTeksStudio(studioField);
        tipeField.getItems().setAll("Reguler", "VIP");
        filterStudio.valueProperty().addListener((obs, lama, baru) -> muat());
        cariField.textProperty().addListener((obs, lama, baru) -> muat());
        tombolUbah.setDisable(true);
        tombolHapus.setDisable(true);
        sembunyikanForm();
    }

    @Override
    public void siap(Admin admin) {
        this.admin = admin;
        dipilih = null;
        filterStudio.getItems().setAll(studioService.cari(""));
        studioField.getItems().setAll(studioService.cari(""));
        if (filterStudio.getItems().isEmpty()) {
            muat();
        } else {
            filterStudio.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void tampilkanFormBaru() {
        idDiedit = null;
        judulForm.setText("Tambah kursi");
        if (filterStudio.getValue() != null) {
            studioField.getSelectionModel().select(filterStudio.getValue());
        } else {
            studioField.getSelectionModel().clearSelection();
        }
        nomorField.clear();
        tipeField.getSelectionModel().select("Reguler");
        errorForm.setText("");
        tampilkanForm();
    }

    @FXML
    private void tampilkanFormUbah() {
        if (dipilih == null) {
            return;
        }
        idDiedit = dipilih.getIdKursi();
        judulForm.setText("Ubah kursi");
        studioField.getItems().stream()
                .filter(studio -> studio.getIdStudio() == dipilih.getIdStudio())
                .findFirst()
                .ifPresent(studioField.getSelectionModel()::select);
        nomorField.setText(dipilih.getNomorKursi());
        tipeField.getSelectionModel().select(dipilih.getTipeKursi());
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
            if (studio != null) {
                filterStudio.getItems().stream()
                        .filter(item -> item.getIdStudio() == studio.getIdStudio())
                        .findFirst()
                        .ifPresent(filterStudio.getSelectionModel()::select);
            }
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
        if (dipilih == null) {
            return;
        }
        if (!Peringatan.konfirmasi("Hapus kursi " + dipilih.getNomorKursi() + "?")) {
            return;
        }
        try {
            kursiService.hapus(dipilih.getIdKursi());
            dipilih = null;
            muat();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            Peringatan.gagal(ex.getMessage());
        }
    }

    private void muat() {
        Studio filter = filterStudio.getValue();
        if (filter == null) {
            tampilan = List.of();
            dipilih = null;
            aturTombol();
            gambarDenah(null);
            return;
        }
        tampilan = kursiService.cari(filter.getIdStudio(), cariField.getText());
        if (dipilih != null) {
            dipilih = tampilan.stream()
                    .filter(kursi -> kursi.getIdKursi() == dipilih.getIdKursi())
                    .findFirst()
                    .orElse(null);
        }
        aturTombol();
        gambarDenah(filter);
    }

    private void aturTombol() {
        boolean ada = dipilih != null;
        tombolUbah.setDisable(!ada);
        tombolHapus.setDisable(!ada);
    }

    private void gambarDenah(Studio studio) {
        wadahDenah.getChildren().clear();
        if (studio == null) {
            wadahDenah.getChildren().add(catatan("Pilih studio untuk melihat denahnya."));
            return;
        }
        VBox kartu = new VBox(12);
        kartu.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #C5CEDA; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 16;");
        kartu.setMaxWidth(Double.MAX_VALUE);
        Label judul = new Label(studio.getNamaStudio());
        judul.getStyleClass().add("judul-form");
        Label jumlah = new Label(tampilan.size() + " kursi");
        jumlah.getStyleClass().add("bantuan");
        kartu.getChildren().addAll(judul, jumlah);
        if (tampilan.isEmpty()) {
            kartu.getChildren().add(catatan("Belum ada kursi di studio ini."));
            wadahDenah.getChildren().add(kartu);
            return;
        }

        Map<String, List<Kursi>> perBaris = new LinkedHashMap<>();
        List<Kursi> tanpaPola = new ArrayList<>();
        for (Kursi kursi : tampilan) {
            String baris = huruf(kursi.getNomorKursi());
            if (baris == null) {
                tanpaPola.add(kursi);
            } else {
                perBaris.computeIfAbsent(baris, kunci -> new ArrayList<>()).add(kursi);
            }
        }
        List<String> urutanBaris = new ArrayList<>(perBaris.keySet());
        urutanBaris.sort(Comparator.comparingInt(KursiController::urutanHuruf));
        int terlebar = perBaris.values().stream().mapToInt(List::size).max().orElse(1);
        terlebar = Math.max(terlebar, tanpaPola.size());
        Label layar = new Label("Layar");
        layar.setAlignment(Pos.CENTER);
        layar.setPrefWidth(terlebar * 48 + Math.max(0, terlebar - 1) * 8);
        layar.setStyle("-fx-background-color: #1A2C50; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 0; -fx-font-weight: 700;");
        VBox papan = new VBox(8);
        papan.setAlignment(Pos.TOP_CENTER);
        papan.setMaxWidth(Double.MAX_VALUE);
        papan.getChildren().add(layar);
        for (String baris : urutanBaris) {
            List<Kursi> kursiBaris = perBaris.get(baris);
            kursiBaris.sort(Comparator.comparingInt(kursi -> angka(kursi.getNomorKursi())));
            papan.getChildren().add(barisKursi(kursiBaris));
        }
        if (!tanpaPola.isEmpty()) {
            papan.getChildren().add(barisKursi(tanpaPola));
        }
        kartu.getChildren().add(papan);
        wadahDenah.getChildren().add(kartu);
    }

    private HBox barisKursi(List<Kursi> kursiBaris) {
        HBox baris = new HBox(8);
        baris.setAlignment(Pos.CENTER);
        baris.setMaxWidth(Double.MAX_VALUE);
        for (Kursi kursi : kursiBaris) {
            baris.getChildren().add(tombolKursi(kursi));
        }
        return baris;
    }

    private Button tombolKursi(Kursi kursi) {
        Button tombol = new Button(kursi.getNomorKursi());
        tombol.setMnemonicParsing(false);
        tombol.setPrefSize(48, 36);
        boolean sedangDipilih = dipilih != null && dipilih.getIdKursi() == kursi.getIdKursi();
        if (sedangDipilih) {
            tombol.setStyle(GAYA_PILIH);
        } else if ("VIP".equalsIgnoreCase(kursi.getTipeKursi())) {
            tombol.setStyle(GAYA_VIP);
        } else {
            tombol.setStyle(GAYA_REGULER);
        }
        tombol.setTooltip(new Tooltip(kursi.getTipeKursi()));
        tombol.setOnAction(event -> {
            dipilih = kursi;
            aturTombol();
            gambarDenah(filterStudio.getValue());
        });
        return tombol;
    }

    private Label catatan(String teks) {
        Label label = new Label(teks);
        label.getStyleClass().add("bantuan");
        label.setWrapText(true);
        return label;
    }

    private static String huruf(String nomor) {
        if (nomor == null) {
            return null;
        }
        String teks = nomor.trim().toUpperCase(Locale.ROOT);
        int batas = 0;
        while (batas < teks.length() && Character.isLetter(teks.charAt(batas))) {
            batas++;
        }
        if (batas == 0 || batas == teks.length() || angkaAtauNol(teks.substring(batas)) < 1) {
            return null;
        }
        return teks.substring(0, batas);
    }

    private static int angka(String nomor) {
        String teks = nomor.trim().toUpperCase(Locale.ROOT);
        int batas = 0;
        while (batas < teks.length() && Character.isLetter(teks.charAt(batas))) {
            batas++;
        }
        return angkaAtauNol(teks.substring(batas));
    }

    private static int angkaAtauNol(String teks) {
        try {
            return Integer.parseInt(teks);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static int urutanHuruf(String huruf) {
        int nilai = 0;
        for (int i = 0; i < huruf.length(); i++) {
            nilai = nilai * 26 + (huruf.charAt(i) - 'A' + 1);
        }
        return nilai;
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
