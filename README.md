==============================================================
 TIKETIN - Sistem Pemesanan Tiket Bioskop
 PBO - Tugas Kelompok II - Kelas A - Kelompok 1
==============================================================

1. DESKRIPSI SINGKAT
--------------------
Tiketin adalah aplikasi desktop pemesanan tiket bioskop dengan konsep
Hybrid Self-Service Cinema. Ada tiga jenis pengguna:
  - Admin    : kelola film (termasuk poster, sutradara, tahun rilis,
               rating usia, rating 1-5), studio, kursi, jadwal tayang
               (status Aktif/Dibatalkan), akun kasir, melihat transaksi,
               laporan penjualan, dan profil admin.
  - Kasir    : melayani pemesanan di loket (pilih film, jadwal, kursi,
               ringkasan, pembayaran tunai, cetak tiket), riwayat
               transaksi, dan profil kasir.
  - Customer : memesan sendiri lewat Mode Kiosk tanpa login (pilih film,
               jadwal, kursi, ringkasan, pembayaran QRIS simulasi, tiket).
Dibuat dengan Java 21, JavaFX 21.0.9 (FXML), Maven, dan MySQL/MariaDB.

2. ANGGOTA KELOMPOK
-------------------
  1. Visvani Bhavanti Jyothi            (2508561003)
  2. I Putu Aditya Widiantara Putra     (2508561006)
  3. Putu Alia Devi Junita Lestari      (2508561009)
  4. I Gusti Agung Gde Raditya Pradana  (2508561011)
Dosen: I Made Satria Bimantara, S.Kom., M.Kom.

3. KEBUTUHAN
------------
  - JDK 21 (cek: java -version)
  - Apache Maven 3.8 atau lebih baru (cek: mvn -v)
  - MySQL 8 atau MariaDB 10.x (bisa lewat XAMPP/Laragon)
  - Koneksi internet saat pertama kali build, karena Maven akan
    mengunduh JavaFX 21.0.9 dan MySQL Connector/J 9.1.0 secara otomatis.
    JavaFX TIDAK perlu di-install manual.

4. LANGKAH MENJALANKAN
----------------------
a) Ekstrak / buka folder proyek (folder yang berisi pom.xml).

b) Import database
   PERHATIAN: tiketin.sql diawali "DROP DATABASE IF EXISTS tiketin",
   jadi database tiketin lama akan dihapus dan dibuat ulang.

   Lewat terminal (dari folder proyek):
     mysql -u root -p -e "source database/tiketin.sql"
   atau:
     mysql -u root -p < database/tiketin.sql

   Lewat phpMyAdmin / MySQL Workbench:
     buka tab Import (phpMyAdmin) atau File > Open SQL Script
     (Workbench), pilih database/tiketin.sql, lalu jalankan.

c) Atur koneksi database
   Salin file contoh:
     src/main/resources/db.properties.example
   menjadi:
     src/main/resources/db.properties
   Lalu isi sesuai MySQL di komputer masing-masing, contoh:
     db.url=jdbc:mysql://localhost:3306/tiketin
     db.user=root
     db.password=isi_password_mysql_anda
   (Kalau root tidak memakai password, kosongkan saja: db.password=)

d) Jalankan aplikasi (dari folder proyek):
     mvn clean javafx:run
   Main class: com.tiketin.App (sudah diatur di pom.xml lewat
   javafx-maven-plugin). Perintah ini sudah dicoba dan aplikasi
   terbuka ke halaman login.

e) Menjalankan dari VS Code
   1. Install extension "Extension Pack for Java" (sudah termasuk
      Maven for Java).
   2. File > Open Folder... pilih folder proyek (yang ada pom.xml).
   3. Pastikan JDK 21 terpilih (Ctrl+Shift+P > "Java: Configure Java
      Runtime").
   4. Buka panel MAVEN di sidebar > tiketin > Plugins > javafx >
      klik "run". Atau buka Terminal di VS Code lalu ketik:
        mvn clean javafx:run
   Catatan: jangan menjalankan App.java dengan tombol "Run" biasa,
   karena JavaFX tidak ikut dimuat dan akan muncul error
   "JavaFX runtime components are missing". Gunakan Maven seperti
   di atas.

5. AKUN DEMO (dari database/tiketin.sql)
----------------------------------------
  Admin : username admin   password admin123
  Kasir : username kasir1  password kasir123
  Customer/Kiosk: tidak perlu login, klik tombol "Mode Kiosk" di
  halaman login.
Akun kasir baru bisa dibuat dari menu Admin > Akun kasir.

6. MIGRASI KOLOM OTOMATIS
-------------------------
Jika memakai database tiketin versi lama (sebelum ada kolom baru),
aplikasi akan menambahkan kolom yang kurang secara otomatis saat
dijalankan:
  - film: sutradara, tahun_rilis, rating_usia, rating
  - jadwal_tayang: status_jadwal (default AKTIF)
Syarat: user database di db.properties punya izin ALTER. Jika tidak,
aplikasi menampilkan pesan error; solusinya import ulang tiketin.sql.

7. STRUKTUR FOLDER
------------------
  pom.xml                         konfigurasi Maven (Java 21, JavaFX)
  database/tiketin.sql            skema + data contoh
  src/main/java/com/tiketin/
    App.java                      class utama (main)
    config/                       koneksi database (DatabaseConnection)
    model/                        class data (Pengguna, Admin, Kasir,
                                  Film, JadwalTayang, Pemesanan, dll.)
    enums/                        KanalPemesanan, StatusJadwal,
                                  StatusPemesanan, StatusPembayaran
    payment/                      interface Pembayaran, PembayaranTunai,
                                  PembayaranQRIS
    dao/                          query SQL (CRUD) ke database
    service/                      logika & validasi aplikasi
    controller/admin|kasir|customer/   controller tiap halaman
    component/                    DenahKursi (denah kursi)
    util/                         helper (sesi, password, poster, dll.)
  src/main/resources/com/tiketin/
    fxml/                         tampilan (FXML)
    css/                          gaya tampilan
    images/                       gambar & poster film (nama file =
                                  judul film)
  src/main/resources/db.properties.example   contoh pengaturan DB
  data/                           dibuat otomatis untuk foto profil
                                  admin (tidak ikut repository)

8. TROUBLESHOOTING
------------------
- "db properties tidak ditemukan di src/main/resources"
    -> File db.properties belum dibuat. Lakukan langkah 4c.

- "Unknown database 'tiketin'"
    -> Database belum di-import. Lakukan langkah 4b.

- "Access denied for user 'root'@'localhost'"
    -> Username/password di db.properties salah. Samakan dengan
       login MySQL Anda (yang dipakai di phpMyAdmin/Workbench).

- "Communications link failure" / "Connection refused"
    -> Server MySQL belum berjalan. Nyalakan MySQL (misalnya lewat
       XAMPP/Laragon) dan pastikan port 3306.

- "JavaFX runtime components are missing"
    -> Aplikasi dijalankan tanpa Maven. Jalankan dengan
       mvn clean javafx:run (lihat langkah 4d/4e).

- "No plugin found for prefix 'javafx'" atau dependency gagal diunduh
    -> Pastikan terhubung internet saat build pertama, dan perintah
       dijalankan dari folder yang berisi pom.xml.

- "invalid target release: 21" / error versi Java
    -> JDK yang dipakai bukan 21. Install JDK 21 dan atur JAVA_HOME.

- Kursi/jadwal tidak muncul di kasir atau kiosk
    -> Hanya jadwal berstatus Aktif dan belum lewat waktunya yang
       ditampilkan. Tambah jadwal baru dari menu Admin > Jadwal tayang.
