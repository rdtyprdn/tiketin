package com.tiketin.controller.kasir;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kasir;
import com.tiketin.service.KasirTransaksiService;
import com.tiketin.util.KasirSession;
import com.tiketin.util.KasirUtil;
import com.tiketin.util.Peringatan;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class KasirPembayaranController {

    @FXML
    private ImageView imgPosterFilm;

    @FXML
    private Label lblJudulFilm;

    @FXML
    private Label lblGenreDurasi;

    @FXML
    private Label lblTanggal;

    @FXML
    private Label lblJamTayang;

    @FXML
    private Label lblStudio;

    @FXML
    private HBox containerKursi;

    @FXML
    private Label lblTotalPembayaran;

    @FXML
    private TextField txtNominalUang;

    @FXML
    private Label lblValidasiPembayaran;

    @FXML
    private Label lblKembalian;

    @FXML
    private Button btnKonfirmasiPembayaran;

    @FXML
    private Label lblIdKasir;

    @FXML
    private Button btnKeluar;

    private final KasirTransaksiService service = new KasirTransaksiService();

    // Total dihitung dari harga jadwal x jumlah kursi
    private double totalPembayaran;

    @FXML
    private void initialize() {
        KasirUtil.pasangHeader(lblIdKasir, btnKeluar);
        totalPembayaran = KasirSession.getTotalBayar();

        Film film = KasirSession.getFilmTerpilih();
        if (film != null) {
            lblJudulFilm.setText(film.getJudul());
            lblGenreDurasi.setText(film.getGenre() + " · " + film.getDurasi() + " menit");
            imgPosterFilm.setImage(KasirUtil.poster(film));
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
        if (KasirSession.getTanggalTerpilih() != null) {
            lblTanggal.setText(KasirSession.getTanggalTerpilih().format(format));
        }
        lblJamTayang.setText(KasirSession.getJamTerpilih());
        lblStudio.setText(KasirSession.getStudioTerpilih());

        KasirRingkasanController.tampilkanKursi(containerKursi, KasirSession.getKursiTerpilih());
        lblTotalPembayaran.setText(KasirUtil.rupiah(totalPembayaran));

        txtNominalUang.textProperty().addListener((observable, oldValue, newValue) -> hitungKembalian());
        hitungKembalian();
    }

    // Boleh ketik "100000" atau "100.000"
    private long bacaNominal() {
        String angka = txtNominalUang.getText() == null ? "" : txtNominalUang.getText().replaceAll("[^0-9]", "");
        if (angka.isEmpty() || angka.length() > 12) {
            return -1;
        }
        return Long.parseLong(angka);
    }

    private void hitungKembalian() {
        long nominal = bacaNominal();

        if (nominal < 0) {
            btnKonfirmasiPembayaran.setDisable(true);
            lblValidasiPembayaran.setText("Masukkan nominal uang yang valid.");
            lblValidasiPembayaran.setStyle("-fx-text-fill: #DC2626;");
            lblKembalian.setText("Rp 0");
        } else if (nominal < totalPembayaran) {
            btnKonfirmasiPembayaran.setDisable(true);
            lblValidasiPembayaran.setText("Nominal pembayaran belum mencukupi.");
            lblValidasiPembayaran.setStyle("-fx-text-fill: #DC2626;");
            lblKembalian.setText("Rp 0");
        } else {
            btnKonfirmasiPembayaran.setDisable(totalPembayaran <= 0);
            lblValidasiPembayaran.setText("Nominal pembayaran mencukupi.");
            lblValidasiPembayaran.setStyle("-fx-text-fill: #16A34A;");
            lblKembalian.setText(KasirUtil.rupiah(nominal - totalPembayaran));
        }
    }

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirRingkasan");
    }

    // Simpan transaksi ke database lewat PemesananService
    @FXML
    private void konfirmasiPembayaran() throws IOException {
        if (btnKonfirmasiPembayaran.isDisabled()) {
            return;
        }

        Kasir kasir = KasirSession.getKasirLogin();
        JadwalTayang jadwal = KasirSession.getJadwalTerpilih();
        if (kasir == null || jadwal == null || KasirSession.getKursiTerpilih().isEmpty()) {
            Peringatan.gagal("Data transaksi belum lengkap. Ulangi dari pilih film.");
            return;
        }

        btnKonfirmasiPembayaran.setDisable(true); // cegah klik dua kali
        try {
            int idPemesanan = service.bayarTunai(jadwal, KasirSession.getKursiTerpilih(), bacaNominal(),
                kasir.getIdKasir());
            KasirSession.setIdPemesananTerakhir(idPemesanan);
            App.setRoot("fxml/kasir/KasirTiket");
        } catch (IllegalArgumentException ex) {
            Peringatan.gagal(ex.getMessage());
            btnKonfirmasiPembayaran.setDisable(false);
        }
    }
}
