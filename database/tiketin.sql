DROP DATABASE IF EXISTS tiketin;
CREATE DATABASE tiketin;
USE tiketin;

CREATE TABLE pengguna (
  id_pengguna INT AUTO_INCREMENT PRIMARY KEY,
  username    VARCHAR(50) NOT NULL UNIQUE,
  password    VARCHAR(64) NOT NULL,
  nama        VARCHAR(100) NOT NULL
);
CREATE TABLE admin (
  id_admin    INT AUTO_INCREMENT PRIMARY KEY,
  id_pengguna INT NOT NULL UNIQUE,
  FOREIGN KEY (id_pengguna) REFERENCES pengguna(id_pengguna)
);
CREATE TABLE kasir (
  id_kasir    INT AUTO_INCREMENT PRIMARY KEY,
  id_pengguna INT NOT NULL UNIQUE,
  FOREIGN KEY (id_pengguna) REFERENCES pengguna(id_pengguna)
);
CREATE TABLE pelanggan (
  id_pelanggan INT AUTO_INCREMENT PRIMARY KEY
);
CREATE TABLE film (
  id_film  INT AUTO_INCREMENT PRIMARY KEY,
  judul    VARCHAR(150) NOT NULL,
  genre    VARCHAR(50),
  durasi   INT NOT NULL,
  sinopsis TEXT,
  id_admin INT NOT NULL,
  FOREIGN KEY (id_admin) REFERENCES admin(id_admin)
);
CREATE TABLE studio (
  id_studio   INT AUTO_INCREMENT PRIMARY KEY,
  nama_studio VARCHAR(50) NOT NULL,
  id_admin    INT NOT NULL,
  FOREIGN KEY (id_admin) REFERENCES admin(id_admin)
);
CREATE TABLE kursi (
  id_kursi    INT AUTO_INCREMENT PRIMARY KEY,
  id_studio   INT NOT NULL,
  nomor_kursi VARCHAR(5) NOT NULL,
  tipe_kursi  VARCHAR(20) NOT NULL DEFAULT 'REGULER',
  id_admin    INT NOT NULL,
  UNIQUE (id_studio, nomor_kursi),
  FOREIGN KEY (id_studio) REFERENCES studio(id_studio),
  FOREIGN KEY (id_admin)  REFERENCES admin(id_admin)
);
CREATE TABLE jadwal_tayang (
  id_jadwal   INT AUTO_INCREMENT PRIMARY KEY,
  id_film     INT NOT NULL,
  id_studio   INT NOT NULL,
  waktu_mulai DATETIME NOT NULL,
  harga_tiket DOUBLE NOT NULL,
  id_admin    INT NOT NULL,
  FOREIGN KEY (id_film)   REFERENCES film(id_film),
  FOREIGN KEY (id_studio) REFERENCES studio(id_studio),
  FOREIGN KEY (id_admin)  REFERENCES admin(id_admin)
);
CREATE TABLE pemesanan (
  id_pemesanan     INT AUTO_INCREMENT PRIMARY KEY,
  id_jadwal        INT NOT NULL,
  id_kasir         INT NULL,
  id_pelanggan     INT NULL,
  waktu_pemesanan  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  kanal_pemesanan  VARCHAR(10) NOT NULL,
  status_pemesanan VARCHAR(20) NOT NULL DEFAULT 'MENUNGGU_PEMBAYARAN',
  total_bayar      DOUBLE NOT NULL,
  FOREIGN KEY (id_jadwal)    REFERENCES jadwal_tayang(id_jadwal),
  FOREIGN KEY (id_kasir)     REFERENCES kasir(id_kasir),
  FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan)
);
CREATE TABLE detail_pemesanan (
  id_detail    INT AUTO_INCREMENT PRIMARY KEY,
  id_pemesanan INT NOT NULL,
  id_jadwal    INT NOT NULL,
  id_kursi     INT NOT NULL,
  harga_tiket  DOUBLE NOT NULL,
  UNIQUE (id_jadwal, id_kursi),
  FOREIGN KEY (id_pemesanan) REFERENCES pemesanan(id_pemesanan) ON DELETE CASCADE,
  FOREIGN KEY (id_jadwal)    REFERENCES jadwal_tayang(id_jadwal),
  FOREIGN KEY (id_kursi)     REFERENCES kursi(id_kursi)
);
CREATE TABLE pembayaran (
  id_pembayaran        INT AUTO_INCREMENT PRIMARY KEY,
  id_pemesanan         INT NOT NULL UNIQUE,
  metode_pembayaran    VARCHAR(10) NOT NULL,
  status_pembayaran    VARCHAR(20) NOT NULL DEFAULT 'BELUM_BAYAR',
  jumlah_bayar         DOUBLE NOT NULL,
  jumlah_uang_diterima DOUBLE NULL,
  kembalian            DOUBLE NULL,
  FOREIGN KEY (id_pemesanan) REFERENCES pemesanan(id_pemesanan) ON DELETE CASCADE
);
CREATE TABLE tiket (
  id_tiket       INT AUTO_INCREMENT PRIMARY KEY,
  id_detail      INT NOT NULL UNIQUE,
  kode_tiket     VARCHAR(20) NOT NULL UNIQUE,
  tanggal_terbit DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status_tiket   VARCHAR(20) NOT NULL DEFAULT 'AKTIF',
  FOREIGN KEY (id_detail) REFERENCES detail_pemesanan(id_detail) ON DELETE CASCADE
);

INSERT INTO pengguna (username, password, nama) VALUES
  ('admin', SHA2('admin123', 256), 'Admin tiketin'),
  ('kasir1', SHA2('kasir123', 256), 'Kasir Satu');
INSERT INTO admin (id_pengguna) VALUES (1);
INSERT INTO kasir (id_pengguna) VALUES (2);
INSERT INTO film (judul, genre, durasi, sinopsis, id_admin) VALUES
  ('Minions 3', 'Animasi', 130, 'Film Minions 3 (Minions & Monsters) mengisahkan petualangan sekelompok Minions di Hollywood era 1920-an yang mendadak menjadi bintang film bisu, namun ketenaran mereka meredup saat era film bersuara (talkies) dimulai karena kendala bahasa Minionese.', 1),
  ('Spider-Man: Brand New Day', 'Adventure/Sci-Fi', 180, 'Kehidupan Baru yang Kesepian: Empat tahun setelah peristiwa No Way Home, dunia telah melupakan identitas Peter Parker (Tom Holland). Peter hidup sendirian sebagai Spider-Man penuh waktu tanpa keluarga, kekasih (MJ), maupun sahabat (Ned) yang mengenalnya lagi.', 1);
INSERT INTO studio (nama_studio, id_admin) VALUES ('Deluxe', 1);
INSERT INTO kursi (id_studio, nomor_kursi, id_admin) VALUES
  (1,'A1',1),(1,'A2',1),(1,'A3',1),(1,'A4',1),(1,'A5',1),
  (1,'B1',1),(1,'B2',1),(1,'B3',1),(1,'B4',1),(1,'B5',1);
INSERT INTO jadwal_tayang (id_film, id_studio, waktu_mulai, harga_tiket, id_admin) VALUES
  (1, 1, '2026-11-16 13:00:00', 40000, 1),
  (2, 1, '2026-11-16 16:00:00', 35000, 1);