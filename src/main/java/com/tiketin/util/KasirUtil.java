package com.tiketin.util;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.model.Kasir;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// Bantuan kecil yang dipakai semua halaman kasir
public final class KasirUtil {

    private KasirUtil() {
    }

    // Isi nama kasir di header dan pasang aksi tombol Keluar
    public static void pasangHeader(Label lblIdKasir, Button btnKeluar) {
        Kasir kasir = KasirSession.getKasirLogin();
        if (lblIdKasir != null && kasir != null) {
            lblIdKasir.setText(kasir.getNama());
        }
        if (btnKeluar != null) {
            btnKeluar.setOnAction(e -> keluar(btnKeluar));
        }
    }

    private static void keluar(Button sumber) {
        if (!Peringatan.konfirmasi("Keluar dari akun kasir?")) {
            return;
        }
        KasirSession.logout();
        try {
            Stage stage = (Stage) sumber.getScene().getWindow();
            stage.setTitle("CineBook");
            App.setRoot("fxml/login");
        } catch (IOException ex) {
            ex.printStackTrace();
            Peringatan.gagal("Halaman login tidak bisa dibuka: " + ex.getMessage());
        }
    }

    // Poster film: logika lengkapnya ada di PosterUtil (dipakai juga oleh kiosk & admin)
    public static Image poster(Film film) {
        return PosterUtil.poster(film);
    }

    public static String namaFilePoster(String judul) {
        return PosterUtil.namaFile(judul);
    }

    // Format Rp tanpa desimal, contoh Rp50.000
    public static String rupiah(double nominal) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        format.setMaximumFractionDigits(0);
        return "Rp" + format.format(nominal);
    }
}
