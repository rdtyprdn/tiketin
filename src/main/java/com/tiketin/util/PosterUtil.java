package com.tiketin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.tiketin.model.Film;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

// Poster film dipakai bersama oleh halaman kasir, kiosk, dan admin.
// Urutan pencarian:
//   1. field poster di objek Film (kalau suatu saat diisi; sekarang tabel film belum punya kolom poster)
//   2. file gambar di src/main/resources/com/tiketin/images/ dengan nama = judul film
//      (contoh "Minions 3.jpg", "Spider-Man Brand New Day.png")
//   3. kalau tidak ada: poster cadangan rapi (kotak navy + judul film)
public final class PosterUtil {

    private static final String FOLDER_GAMBAR = "/com/tiketin/images/";
    private static final String[] EKSTENSI = { ".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG" };
    private static final double LEBAR_CADANGAN = 300;
    private static final double TINGGI_CADANGAN = 450;

    private static final Map<String, Image> CACHE_CADANGAN = new HashMap<>();

    private PosterUtil() {
    }

    // Selalu mengembalikan gambar (poster asli atau cadangan)
    public static Image poster(Film film) {
        Image asli = cari(film);
        if (asli != null) {
            return asli;
        }
        return cadangan(film == null ? null : film.getJudul());
    }

    // Hanya poster asli; null kalau file tidak ditemukan
    public static Image cari(Film film) {
        if (film == null) {
            return null;
        }
        if (film.getPoster() != null && !film.getPoster().isBlank()) {
            Image img = muat(film.getPoster().trim());
            if (img != null) {
                return img;
            }
        }
        if (film.getJudul() == null || film.getJudul().isBlank()) {
            return null;
        }
        for (String nama : variasiNama(film.getJudul())) {
            for (String ext : EKSTENSI) {
                Image img = muat(nama + ext);
                if (img != null) {
                    return img;
                }
            }
        }
        return null;
    }

    // Judul -> nama file, contoh "Spider-Man: Brand New Day" -> "Spider-Man Brand New Day"
    public static String namaFile(String judul) {
        return judul.replaceAll("[^A-Za-z0-9 &\\-]", "").replaceAll("\\s+", " ").trim();
    }

    // Beberapa variasi nama supaya salah ketik kecil (huruf besar/kecil, spasi, tanda baca) tetap ketemu
    private static Set<String> variasiNama(String judul) {
        Set<String> hasil = new LinkedHashSet<>();
        String bersih = namaFile(judul);
        hasil.add(bersih);                                        // Spider-Man Brand New Day
        hasil.add(judul.trim());                                  // judul persis
        hasil.add(bersih.toLowerCase(Locale.ROOT));               // spider-man brand new day
        hasil.add(bersih.replace("&", "dan"));                    // Rangga dan Cinta
        hasil.add(bersih.replace(' ', '-').toLowerCase(Locale.ROOT)); // spider-man-brand-new-day
        hasil.add(bersih.replace(' ', '_'));                      // Spider-Man_Brand_New_Day
        hasil.removeIf(String::isBlank);
        return hasil;
    }

    private static Image muat(String namaFile) {
        // jangan sampai keluar dari folder gambar
        if (namaFile.contains("..") || namaFile.contains("/") || namaFile.contains("\\")) {
            return null;
        }
        try (InputStream in = PosterUtil.class.getResourceAsStream(FOLDER_GAMBAR + namaFile)) {
            if (in == null) {
                return null;
            }
            Image img = new Image(in);
            return img.isError() ? null : img;
        } catch (IOException e) {
            return null;
        }
    }

    // Poster cadangan: kotak navy, garis kuning, judul film di tengah
    public static Image cadangan(String judul) {
        String teks = judul == null || judul.isBlank() ? "Poster belum tersedia" : judul.trim();
        return CACHE_CADANGAN.computeIfAbsent(teks, PosterUtil::gambarCadangan);
    }

    private static Image gambarCadangan(String judul) {
        Label merek = new Label("tiketin");
        merek.setTextFill(Color.web("#FBC436"));
        merek.setFont(Font.font("Inter", FontWeight.BOLD, 26));

        Region garis = new Region();
        garis.setPrefSize(60, 4);
        garis.setMaxSize(60, 4);
        garis.setStyle("-fx-background-color: #FBC436; -fx-background-radius: 2;");

        Label judulLabel = new Label(judul);
        judulLabel.setTextFill(Color.WHITE);
        judulLabel.setFont(Font.font("Inter", FontWeight.BOLD, 30));
        judulLabel.setWrapText(true);
        judulLabel.setTextAlignment(TextAlignment.CENTER);
        judulLabel.setAlignment(Pos.CENTER);
        judulLabel.setMaxWidth(LEBAR_CADANGAN - 50);

        Label keterangan = new Label("Poster belum tersedia");
        keterangan.setTextFill(Color.web("#A9B4C8"));
        keterangan.setFont(Font.font("Inter", 14));

        VBox isi = new VBox(18, merek, garis, judulLabel, keterangan);
        isi.setAlignment(Pos.CENTER);
        isi.setPadding(new Insets(25));

        StackPane kotak = new StackPane(isi);
        kotak.setPrefSize(LEBAR_CADANGAN, TINGGI_CADANGAN);
        kotak.setMinSize(LEBAR_CADANGAN, TINGGI_CADANGAN);
        kotak.setMaxSize(LEBAR_CADANGAN, TINGGI_CADANGAN);
        kotak.setStyle("-fx-background-color: linear-gradient(to bottom, #1A2C50, #263F70);");

        try {
            SnapshotParameters param = new SnapshotParameters();
            param.setFill(Color.web("#1A2C50"));
            new Scene(kotak); // scene sementara supaya CSS & teks label ikut tergambar
            kotak.applyCss();
            kotak.layout();
            WritableImage hasil = kotak.snapshot(param, null);
            return hasil;
        } catch (RuntimeException e) {
            // snapshot butuh thread JavaFX; kalau gagal, pakai ikon tiket lama
            Image ikon = muat("Blue Ticket Icon with Perforations.png");
            return ikon != null ? ikon : new WritableImage(1, 1);
        }
    }
}
