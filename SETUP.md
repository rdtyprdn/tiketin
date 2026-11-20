# Setup Tiketin di laptop sendiri (macOS & Windows)

Panduan singkat supaya project Tiketin bisa jalan di laptop masing-masing.
Kalau ada langkah yang error, lihat bagian **Error yang sering muncul** di bawah.

Yang dibutuhkan:
- **JDK 21** (Java)
- **Maven** (project ini belum punya `mvnw`, jadi Maven harus di-install)
- **MySQL** 8 (atau MariaDB, misalnya dari XAMPP)
- **Git**

JavaFX **tidak perlu** di-install terpisah. Maven otomatis mengunduh JavaFX 21.0.9 saat pertama kali dijalankan.

---

## 1. Install JDK 21 dan Maven

### macOS (pakai Homebrew)
Kalau belum punya Homebrew, install dulu dari https://brew.sh. Lalu di Terminal:

```bash
brew install --cask temurin@21
brew install maven
```

Supaya Maven memakai Java 21, tambahkan baris ini ke `~/.zshrc`, lalu tutup dan buka lagi Terminal:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Windows
1. **JDK 21**: buka PowerShell, jalankan
   `winget install --id EclipseAdoptium.Temurin.21.JDK`
   (atau download installer "Temurin 21 LTS" dari https://adoptium.net, centang opsi **Set JAVA_HOME** saat install).
2. **Maven**: download `apache-maven-3.9.x-bin.zip` dari https://maven.apache.org/download.cgi,
   extract ke misalnya `C:\maven`, lalu tambahkan `C:\maven\apache-maven-3.9.x\bin` ke **Path**
   (Start → "Edit the system environment variables" → Environment Variables → Path → New).
3. Pastikan ada variabel **JAVA_HOME** yang menunjuk ke folder JDK 21
   (contoh `C:\Program Files\Eclipse Adoptium\jdk-21...`, folder yang di dalamnya ada `bin`).

### Cek (macOS & Windows)
Buka terminal **baru**, lalu:

```bash
java -version
mvn -version
```

Keduanya harus menyebut versi **21**. Kalau `mvn -version` menyebut Java lain, `JAVA_HOME` belum benar.

---

## 2. Install MySQL

- **macOS**: `brew install mysql` lalu `brew services start mysql`.
  Atur password root: `mysql_secure_installation` (ikuti pertanyaannya).
- **Windows**: download **MySQL Installer** dari https://dev.mysql.com/downloads/installer/,
  pilih "Server only" (atau tambah MySQL Workbench), dan catat password root yang kamu buat.
  Pakai XAMPP juga boleh: nyalakan **MySQL** di XAMPP Control Panel (user `root`, password kosong).

---

## 3. Ambil project terbaru

Kalau belum pernah clone:

```bash
git clone https://github.com/rdtyprdn/tiketin.git
cd tiketin
```

Kalau sudah pernah clone, masuk ke folder `tiketin` lalu:

```bash
git checkout main
git pull origin main
```

---

## 4. Buat database `tiketin`

Schema + data contoh ada di file **`database/tiketin.sql`**.

⚠️ File ini diawali `DROP DATABASE IF EXISTS tiketin;`, artinya **database `tiketin` lama akan dihapus dan dibuat ulang**.
Kalau ada data penting di database lama, backup dulu.

Jalankan dari folder `tiketin` (ganti `root` kalau user MySQL kamu beda):

```bash
mysql -u root -p -e "source database/tiketin.sql"
```

- Windows: kalau `mysql` tidak dikenal, pakai path lengkap, misalnya
  `"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p -e "source database/tiketin.sql"`
  (XAMPP: `C:\xampp\mysql\bin\mysql.exe`).
- Atau pakai **MySQL Workbench**: File → Open SQL Script → pilih `database/tiketin.sql` → klik ⚡ (Execute).

Cek hasilnya:

```bash
mysql -u root -p -e "USE tiketin; SHOW TABLES;"
```

Harus muncul tabel `pengguna`, `film`, `jadwal_tayang`, `pemesanan`, `tiket`, dan lain-lain.

---

## 5. Isi `db.properties` (koneksi database)

Copy file contoh lalu isi:

- macOS: `cp src/main/resources/db.properties.example src/main/resources/db.properties`
- Windows (PowerShell): `Copy-Item src\main\resources\db.properties.example src\main\resources\db.properties`

Buka `src/main/resources/db.properties`, isi sesuai MySQL di laptopmu:

```properties
db.url=jdbc:mysql://localhost:3306/tiketin
db.user=root
db.password=password_mysql_kamu
```

(XAMPP: `db.password=` dikosongkan.)

🔒 **Jangan pernah commit `db.properties`.** File ini sudah ada di `.gitignore`.
Sebelum commit, selalu cek `git status`; kalau `db.properties` muncul, jangan di-`git add`.

---

## 6. Jalankan aplikasi

Dari folder `tiketin`:

```bash
mvn clean javafx:run
```

Pertama kali agak lama karena Maven mengunduh library (JavaFX, MySQL connector).

### Akun contoh (dari `database/tiketin.sql`)
| Peran | Username | Password |
|-------|----------|----------|
| Admin | `admin`  | `admin123` |
| Kasir | `kasir1` | `kasir123` |

**Kiosk** tidak perlu login: klik tombol **Mode Kiosk** di halaman login.
Untuk keluar dari kiosk, tekan **Esc** di halaman awal kiosk.

### Coba alurnya
1. Login `kasir1` → Mulai Transaksi → pilih film → jadwal → kursi → bayar tunai → tiket keluar.
2. Mode Kiosk → Mulai Pesan → pilih film → jadwal → kursi → QRIS → Konfirmasi → e-ticket.
3. Login `admin` → Transaksi / Laporan: dua transaksi tadi muncul (kanal Kasir dan Kiosk).

File SQL sudah berisi jadwal contoh untuk besok dan lusa (dihitung dari tanggal kamu import).
Kalau jadwalnya sudah lewat, tambah jadwal baru lewat halaman **Admin → Jadwal tayang**.

### Poster film
Taruh gambar poster di `src/main/resources/com/tiketin/images/` dengan nama **sama dengan judul film**
(tanda baca seperti `:` dihapus), format `.jpg`, `.jpeg`, atau `.png`. Contoh:
- `Minions 3.jpg`
- `Spider-Man Brand New Day.jpg`

Kalau tidak ada, aplikasi menampilkan poster cadangan (kotak biru tua + judul film).

---

## Error yang sering muncul

**`Error: JavaFX runtime components are missing`** atau error `module javafx.controls not found`
→ Jangan jalankan lewat tombol ▶ "Run Java" di VS Code / dengan `java -jar`. Jalankan pakai `mvn clean javafx:run`.

**`mvn -version` menunjukkan Java 17 / Java 23** atau error `release version 21 not supported`
→ `JAVA_HOME` belum menunjuk ke JDK 21. Perbaiki (lihat langkah 1), lalu buka terminal baru.

**`'mvn' is not recognized` / `command not found: mvn`**
→ Maven belum masuk Path (Windows) atau belum di-install (`brew install maven`). Buka terminal baru setelah install.

**`db properties tidak ditemukan di src/main/resources`**
→ Kamu belum membuat `src/main/resources/db.properties` (langkah 5). Namanya harus persis `db.properties`, bukan `db.properties.txt`
(Windows: aktifkan "File name extensions" di File Explorer supaya kelihatan).

**`Access denied for user 'root'@'localhost'`**
→ Username/password di `db.properties` salah. Cek dengan `mysql -u root -p` pakai password yang sama.

**`Unknown database 'tiketin'`**
→ Database belum dibuat. Ulangi langkah 4.

**`Communications link failure` / `Connection refused`**
→ MySQL belum menyala. macOS: `brew services start mysql`. Windows: nyalakan service MySQL (atau MySQL di XAMPP).

**Gagal tambah/ubah akun kasir, error `Data too long for column 'password'`**
→ Database kamu masih versi lama (kolom password terlalu pendek). Jalankan sekali:

```sql
ALTER TABLE pengguna MODIFY password VARCHAR(255) NOT NULL;
```

(Database baru dari `database/tiketin.sql` versi terbaru sudah 255, jadi tidak perlu.)

**Halaman jadwal kasir/kiosk kosong**
→ Jadwal yang tampil hanya yang belum lewat. Tambah jadwal di Admin → Jadwal tayang.

**`git pull` gagal karena ada perubahan lokal**
→ Simpan dulu perubahanmu: `git stash`, lalu `git pull origin main`, lalu `git stash pop`.
Kalau bingung, tanya di grup sebelum menjalankan perintah yang menghapus perubahan.
