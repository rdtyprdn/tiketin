package com.tiketin.controller.customer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kursi;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.KioskSession;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class KioskOrderSummaryController {

    private static final DateTimeFormatter FORMAT_TANGGAL =
        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("id-ID"));
    private static final DateTimeFormatter FORMAT_JAM = DateTimeFormatter.ofPattern("HH.mm");

    // DETAIL FILM
    @FXML private ImageView imgPosterSummary;
    @FXML private Label lblJudulFilmSummary;
    @FXML private Label lblTanggalSummary;
    @FXML private Label lblJamSummary;
    @FXML private Label lblStudioSummary;
    @FXML private Label lblKursiSummary;

    // RINGKASAN PESANAN
    @FXML private Label lblJumlahTiketSummary;
    @FXML private Label lblHargaTiketSummary;
    @FXML private Label lblTotalPembayaran;

    // Semua data diambil dari pilihan pelanggan (KioskSession)
    @FXML
    private void initialize() {
        Film film = KioskSession.getFilmTerpilih();
        JadwalTayang jadwal = KioskSession.getJadwalTerpilih();
        List<Kursi> kursi = KioskSession.getKursiTerpilih();
        if (film == null || jadwal == null || kursi.isEmpty()) {
            return; // belum ada pesanan: biarkan tampilan contoh
        }

        lblJudulFilmSummary.setText(film.getJudul());
        lblTanggalSummary.setText(jadwal.getWaktuMulai().format(FORMAT_TANGGAL));
        lblJamSummary.setText(jadwal.getWaktuMulai().format(FORMAT_JAM));
        lblStudioSummary.setText(KioskSession.getStudioTerpilih());
        lblKursiSummary.setText(kursi.stream().map(Kursi::getNomorKursi).collect(Collectors.joining(", ")));
        lblKursiSummary.setWrapText(true);
        imgPosterSummary.setImage(KasirUtil.poster(film));

        lblJumlahTiketSummary.setText(kursi.size() + " Tiket");
        lblHargaTiketSummary.setText(KasirUtil.rupiah(jadwal.getHargaTiket()));
        lblTotalPembayaran.setText(KasirUtil.rupiah(KioskSession.getTotalBayar()));
    }

    // KEMBALI KE HALAMAN PILIH KURSI
    @FXML
    private void handleKembali() {
        try {
            App.setRoot("fxml/customer/kiosk-seat-selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LANJUT KE PEMBAYARAN QRIS
    @FXML
    private void handleLanjutPembayaran() {
        if (KioskSession.getKursiTerpilih().isEmpty()) {
            return;
        }
        try {
            App.setRoot("fxml/customer/kiosk-qris-simulation");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
