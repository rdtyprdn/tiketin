package com.tiketin.controller.kasir;

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
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class KasirKursiController {

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    @FXML
    private Label lblInfoJadwal;

    @FXML
    private VBox wadahDenah;

    @FXML
    private Label lblKursiDipilih;

    @FXML
    private Label lblTotal;

    private final DenahKursi denah = new DenahKursi();

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);

        Film film = KasirSession.getFilmTerpilih();
        JadwalTayang jadwal = KasirSession.getJadwalTerpilih();
        if (film == null || jadwal == null) {
            lblInfoJadwal.setText("Jadwal belum dipilih.");
            return;
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
        lblInfoJadwal.setText(film.getJudul() + " · " + jadwal.getWaktuMulai().format(format)
            + " · " + KasirSession.getJamTerpilih() + " · " + KasirSession.getStudioTerpilih()
            + " · " + KasirUtil.rupiah(jadwal.getHargaTiket()) + "/kursi");

        // Denah kursi dari komponen DenahKursi
        denah.setSaatBerubah(this::perbaruiRingkasan);
        try {
            denah.muat(jadwal.getIdStudio(), jadwal.getIdJadwal());
        } catch (SQLException ex) {
            Peringatan.gagal("Gagal memuat kursi: " + ex.getMessage());
        }
        if (denah.getJumlahKursi() == 0) {
            wadahDenah.getChildren().add(new Label("Studio ini belum punya kursi. Tambahkan lewat halaman Admin."));
        } else {
            denah.setMaxWidth(Region.USE_PREF_SIZE); // denah di tengah, sama seperti kiosk
            wadahDenah.getChildren().add(denah);
        }
        perbaruiRingkasan();
    }

    // Update teks kursi dipilih dan total setiap klik kursi
    private void perbaruiRingkasan() {
        List<Kursi> dipilih = denah.getKursiDipilihLengkap();
        JadwalTayang jadwal = KasirSession.getJadwalTerpilih();
        double harga = jadwal == null ? 0 : jadwal.getHargaTiket();

        if (dipilih.isEmpty()) {
            lblKursiDipilih.setText("Belum ada kursi dipilih");
        } else {
            lblKursiDipilih.setText(dipilih.stream().map(Kursi::getNomorKursi).collect(Collectors.joining(", ")));
        }
        lblTotal.setText(KasirUtil.rupiah(harga * dipilih.size()));
    }

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirJadwal");
    }

    @FXML
    private void lanjut() throws IOException {
        List<Kursi> dipilih = denah.getKursiDipilihLengkap();
        if (dipilih.isEmpty()) {
            Peringatan.gagal("Pilih minimal satu kursi.");
            return;
        }
        KasirSession.setKursiTerpilih(dipilih);
        App.setRoot("fxml/kasir/KasirRingkasan");
    }
}
