package com.tiketin.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.tiketin.App;
import com.tiketin.component.DenahKursi;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kursi;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.KioskSession;
import com.tiketin.util.Peringatan;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// Halaman baru: pilih kursi di kiosk (memakai komponen DenahKursi yang sama dengan kasir)
public class KioskSeatSelectionController {

    private static final DateTimeFormatter FORMAT_TANGGAL =
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
    private static final DateTimeFormatter FORMAT_JAM = DateTimeFormatter.ofPattern("HH.mm");

    @FXML private Label lblInfoJadwal;
    @FXML private VBox wadahDenah;
    @FXML private Label lblKursiDipilih;
    @FXML private Label lblHargaSatuan;
    @FXML private Label lblTotal;
    @FXML private Button btnLanjut;

    private final DenahKursi denah = new DenahKursi();

    @FXML
    private void initialize() {
        btnLanjut.setDisable(true);

        Film film = KioskSession.getFilmTerpilih();
        JadwalTayang jadwal = KioskSession.getJadwalTerpilih();
        if (film == null || jadwal == null) {
            lblInfoJadwal.setText("Jadwal belum dipilih.");
            return;
        }

        lblInfoJadwal.setText(film.getJudul() + "  ·  " + jadwal.getWaktuMulai().format(FORMAT_TANGGAL)
            + "  ·  " + jadwal.getWaktuMulai().format(FORMAT_JAM)
            + "  ·  " + KioskSession.getStudioTerpilih());
        lblHargaSatuan.setText(KasirUtil.rupiah(jadwal.getHargaTiket()) + " / kursi");

        // Kursi yang sudah terjual otomatis abu-abu dan tidak bisa diklik
        denah.setSaatBerubah(this::perbaruiRingkasan);
        try {
            denah.muat(jadwal.getIdStudio(), jadwal.getIdJadwal());
        } catch (SQLException ex) {
            Peringatan.gagal("Gagal memuat kursi: " + ex.getMessage());
        }
        if (denah.getJumlahKursi() == 0) {
            wadahDenah.getChildren().add(new Label("Studio ini belum punya kursi."));
        } else {
            denah.setMaxWidth(Region.USE_PREF_SIZE); // denah di tengah
            wadahDenah.getChildren().add(denah);
        }

        // pilihan kursi lama tidak dibawa (sengaja, biar sederhana)
        KioskSession.setKursiTerpilih(null);
        perbaruiRingkasan();
    }

    private void perbaruiRingkasan() {
        List<Kursi> dipilih = denah.getKursiDipilihLengkap();
        JadwalTayang jadwal = KioskSession.getJadwalTerpilih();
        double harga = jadwal == null ? 0 : jadwal.getHargaTiket();

        lblKursiDipilih.setText(dipilih.isEmpty()
            ? "Belum ada kursi dipilih"
            : dipilih.stream().map(Kursi::getNomorKursi).collect(Collectors.joining(", ")));
        lblTotal.setText(KasirUtil.rupiah(harga * dipilih.size()));
        btnLanjut.setDisable(dipilih.isEmpty());
    }

    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/kiosk-schedule-selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLanjut() {
        List<Kursi> dipilih = denah.getKursiDipilihLengkap();
        if (dipilih.isEmpty()) {
            return;
        }
        KioskSession.setKursiTerpilih(dipilih);
        try {
            App.setRoot("fxml/customer/kiosk-order-summary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
