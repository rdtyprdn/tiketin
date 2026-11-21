package com.tiketin.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
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
//
// Upload poster dari Admin > Film menyalin file ke src/main/resources/com/tiketin/images/
// dengan nama = judul film (jadi bisa di-commit dan dipakai seluruh tim), dan juga ke
// target/classes kalau ada. Saat dicari, folder sumber dicek dulu sehingga poster baru
// langsung tampil tanpa build ulang.
public final class PosterUtil {

    private static final String FOLDER_GAMBAR = "/com/tiketin/images/";
    private static final String[] EKSTENSI = { ".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG" };
    private static final String FOLDER_SUMBER = "src/main/resources/com/tiketin/images";
    private static final long UKURAN_MAKS = 5L * 1024 * 1024;
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

    private static boolean namaAman(String namaFile) {
        return !namaFile.isBlank() && !namaFile.contains("..") && !namaFile.contains("/") && !namaFile.contains("\\");
    }

    private static Image muat(String namaFile) {
        // jangan sampai keluar dari folder gambar
        if (!namaAman(namaFile)) {
            return null;
        }
        // 1) folder sumber proyek (hasil upload terbaru, tanpa perlu build ulang)
        for (Path folder : folderTulis()) {
            Path file = resolve(folder, namaFile);
            if (file != null && Files.isRegularFile(file)) {
                try (InputStream in = Files.newInputStream(file)) {
                    Image img = new Image(in);
                    if (!img.isError()) {
                        return img;
                    }
                } catch (IOException e) {
                    // lanjut ke classpath
                }
            }
        }
        // 2) classpath (target/classes atau jar)
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

    private static Path resolve(Path folder, String namaFile) {
        try {
            return folder.resolve(namaFile);
        } catch (InvalidPathException e) {
            return null;
        }
    }

    // ===================== Upload / hapus / ganti nama poster (Admin > Film) =====================

    // Folder src/main/resources/com/tiketin/images di proyek; null kalau tidak ketemu (misal jalan dari jar)
    public static Path folderSumber() {
        Path dariKerja = Path.of(System.getProperty("user.dir"), FOLDER_SUMBER);
        if (Files.isDirectory(dariKerja)) {
            return dariKerja;
        }
        Path kelas = folderKelas();
        if (kelas != null) {
            // .../target/classes/com/tiketin/images -> .../src/main/resources/com/tiketin/images
            Path akar = kelas.getParent();
            for (int i = 0; i < 4 && akar != null; i++) {
                akar = akar.getParent();
            }
            if (akar != null && Files.isDirectory(akar.resolve(FOLDER_SUMBER))) {
                return akar.resolve(FOLDER_SUMBER);
            }
        }
        return null;
    }

    // Folder gambar di classpath kalau berupa folder biasa (target/classes/com/tiketin/images)
    private static Path folderKelas() {
        URL url = PosterUtil.class.getResource(FOLDER_GAMBAR);
        if (url == null || !"file".equals(url.getProtocol())) {
            return null;
        }
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException | IllegalArgumentException e) {
            return null;
        }
    }

    // Semua folder yang ditulisi saat upload (sumber dulu, lalu target/classes)
    private static java.util.List<Path> folderTulis() {
        java.util.List<Path> hasil = new java.util.ArrayList<>();
        Path sumber = folderSumber();
        if (sumber != null) {
            hasil.add(sumber);
        }
        Path kelas = folderKelas();
        if (kelas != null && Files.isDirectory(kelas) && (sumber == null || !sameFile(sumber, kelas))) {
            hasil.add(kelas);
        }
        return hasil;
    }

    private static boolean sameFile(Path a, Path b) {
        try {
            return Files.isSameFile(a, b);
        } catch (IOException e) {
            return a.toAbsolutePath().normalize().equals(b.toAbsolutePath().normalize());
        }
    }

    // Ekstensi .jpg/.jpeg/.png dari nama file (huruf kecil); null kalau bukan gambar yang didukung
    public static String ekstensi(Path file) {
        String nama = file.getFileName().toString().toLowerCase(Locale.ROOT);
        for (String ext : new String[] { ".jpg", ".jpeg", ".png" }) {
            if (nama.endsWith(ext)) {
                return ext;
            }
        }
        return null;
    }

    // Cek file sebelum dipakai: format, ukuran, dan bisa dibaca sebagai gambar
    public static Image bacaFile(Path file) {
        if (file == null || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("File poster tidak ditemukan.");
        }
        if (ekstensi(file) == null) {
            throw new IllegalArgumentException("Poster harus berformat JPG, JPEG, atau PNG.");
        }
        try {
            if (Files.size(file) > UKURAN_MAKS) {
                throw new IllegalArgumentException("Ukuran poster maksimal 5 MB.");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File poster tidak bisa dibaca.");
        }
        try (InputStream in = Files.newInputStream(file)) {
            Image img = new Image(in);
            if (img.isError()) {
                throw new IllegalArgumentException("File poster bukan gambar yang valid.");
            }
            return img;
        } catch (IOException e) {
            throw new IllegalArgumentException("File poster tidak bisa dibaca.");
        }
    }

    // Simpan poster untuk judul ini (menggantikan poster lama judul yang sama). Hasil: nama file.
    public static String simpanPoster(String judul, Path file) {
        bacaFile(file);
        String nama = namaFile(judul);
        if (nama.isBlank()) {
            throw new IllegalArgumentException("Judul film tidak bisa dipakai sebagai nama file poster.");
        }
        java.util.List<Path> folder = folderTulis();
        if (folder.isEmpty()) {
            throw new IllegalArgumentException("Folder gambar proyek tidak ditemukan. Jalankan aplikasi dari folder proyek (mvn javafx:run).");
        }
        String namaBaru = nama + ekstensi(file);
        try {
            byte[] isi = Files.readAllBytes(file); // baca dulu: file sumber bisa saja poster lama yang akan dihapus
            hapusPoster(judul);
            for (Path f : folder) {
                Files.write(f.resolve(namaBaru), isi);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Poster gagal disimpan: " + e.getMessage());
        }
        return namaBaru;
    }

    // Hapus semua file poster milik judul ini (semua variasi nama & ekstensi) dari folder proyek
    public static void hapusPoster(String judul) {
        if (judul == null || judul.isBlank()) {
            return;
        }
        for (Path folder : folderTulis()) {
            for (String nama : variasiNama(judul)) {
                for (String ext : EKSTENSI) {
                    if (!namaAman(nama + ext)) {
                        continue;
                    }
                    Path file = resolve(folder, nama + ext);
                    try {
                        if (file != null) {
                            Files.deleteIfExists(file);
                        }
                    } catch (IOException e) {
                        // abaikan, file lain tetap dicoba
                    }
                }
            }
        }
    }

    // Judul film diganti: poster berbasis judul lama ikut dipindah ke nama judul baru
    public static void gantiJudul(String judulLama, String judulBaru) {
        if (judulLama == null || judulBaru == null || namaFile(judulLama).equals(namaFile(judulBaru))) {
            return;
        }
        Film baru = new Film();
        baru.setJudul(judulBaru);
        if (cariFileSumber(judulBaru) != null || cari(baru) != null) {
            return; // judul baru sudah punya poster sendiri, jangan ditimpa
        }
        Path lama = cariFileSumber(judulLama);
        if (lama == null) {
            return; // poster lama hanya ada di classpath/jar atau tidak ada sama sekali
        }
        simpanPoster(judulBaru, lama);
        hapusPoster(judulLama);
    }

    // File poster di folder proyek untuk judul ini; null kalau tidak ada
    private static Path cariFileSumber(String judul) {
        for (Path folder : folderTulis()) {
            for (String nama : variasiNama(judul)) {
                for (String ext : EKSTENSI) {
                    if (!namaAman(nama + ext)) {
                        continue;
                    }
                    Path file = resolve(folder, nama + ext);
                    if (file != null && Files.isRegularFile(file)) {
                        return file;
                    }
                }
            }
        }
        return null;
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
