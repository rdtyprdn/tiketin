package com.tiketin.util;

import java.io.IOException;
import java.io.InputStream;
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

    private static final String FOLDER_GAMBAR = "/com/tiketin/images/";
    private static final String POSTER_CADANGAN = "Blue Ticket Icon with Perforations.png";
    private static final String[] EKSTENSI = { ".jpg", ".png", ".jpeg" };

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

    // Poster: pakai field poster kalau ada, kalau tidak cari file sesuai judul,
    // kalau tetap tidak ada pakai gambar cadangan
    public static Image poster(Film film) {
        if (film != null) {
            if (film.getPoster() != null && !film.getPoster().isBlank()) {
                Image img = muatGambar(film.getPoster());
                if (img != null) {
                    return img;
                }
            }
            if (film.getJudul() != null) {
                String nama = namaFilePoster(film.getJudul());
                for (String ext : EKSTENSI) {
                    Image img = muatGambar(nama + ext);
                    if (img != null) {
                        return img;
                    }
                }
            }
        }
        return muatGambar(POSTER_CADANGAN);
    }

    // Judul -> nama file, contoh "Spider-Man: Brand New Day" -> "Spider-Man Brand New Day"
    public static String namaFilePoster(String judul) {
        return judul.replaceAll("[^A-Za-z0-9 &\\-]", "").replaceAll("\\s+", " ").trim();
    }

    private static Image muatGambar(String namaFile) {
        InputStream in = KasirUtil.class.getResourceAsStream(FOLDER_GAMBAR + namaFile);
        return in == null ? null : new Image(in);
    }

    // Format Rp tanpa desimal, contoh Rp50.000
    public static String rupiah(double nominal) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        format.setMaximumFractionDigits(0);
        return "Rp" + format.format(nominal);
    }
}
