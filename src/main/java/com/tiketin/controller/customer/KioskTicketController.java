package com.tiketin.controller.customer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.KasirTiketItem;
import com.tiketin.service.KioskService;
import com.tiketin.util.KioskSession;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KioskTicketController {

    private static final DateTimeFormatter FORMAT_TANGGAL =
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
    private static final DateTimeFormatter FORMAT_JAM = DateTimeFormatter.ofPattern("HH.mm");

    @FXML
    private Label lblKodeTiket;

    @FXML
    private Label lblJudulFilmTiket;

    @FXML
    private Label lblTanggalTiket;

    @FXML
    private Label lblJamTiket;

    @FXML
    private Label lblStudioTiket;

    @FXML
    private Label lblKursiTiket;

    @FXML
    private Label lblJumlahTiket;

    private final KioskService service = new KioskService();

    // Data e-ticket diambil dari pesanan yang baru disimpan di database
    @FXML
    private void initialize() {
        Integer idPemesanan = KioskSession.getIdPemesananTerakhir();
        Film film = KioskSession.getFilmTerpilih();
        JadwalTayang jadwal = KioskSession.getJadwalTerpilih();
        if (idPemesanan == null || film == null || jadwal == null) {
            return; // belum ada pesanan: biarkan tampilan contoh
        }

        List<KasirTiketItem> tiket;
        try {
            tiket = service.tiket(idPemesanan);
        } catch (IllegalArgumentException ex) {
            lblKodeTiket.setText(ex.getMessage());
            return;
        }

        lblKodeTiket.setText(tiket.stream().map(KasirTiketItem::getKodeTiket).collect(Collectors.joining(", ")));
        lblKodeTiket.setWrapText(true);
        lblJudulFilmTiket.setText(film.getJudul());
        lblTanggalTiket.setText(jadwal.getWaktuMulai().format(FORMAT_TANGGAL));
        lblJamTiket.setText(jadwal.getWaktuMulai().format(FORMAT_JAM));
        lblStudioTiket.setText(KioskSession.getStudioTerpilih());
        lblKursiTiket.setText(tiket.stream().map(KasirTiketItem::getKursi).collect(Collectors.joining(", ")));
        lblKursiTiket.setWrapText(true);
        lblJumlahTiket.setText(String.valueOf(tiket.size()));
    }

    // Selesai: kembali ke halaman awal kiosk (pilihan dikosongkan di sana)
    @FXML
    private void handleSelesai() {
        try {
            App.setRoot("fxml/customer/kiosk-home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
