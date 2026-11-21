package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.enums.StatusJadwal;
import com.tiketin.model.JadwalTayang;

public class JadwalTayangDAO {

    // Kolom status_jadwal baru ditambahkan belakangan. Supaya database tim yang sudah ada
    // tidak perlu di-import ulang, kolomnya dibuat otomatis sekali saat aplikasi pertama kali
    // memakai tabel ini (database baru dari tiketin.sql sudah punya kolom ini).
    private static volatile boolean kolomStatusSiap = false;

    private static Connection koneksi() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (!kolomStatusSiap) {
            pastikanKolomStatus(conn);
        }
        return conn;
    }

    private static synchronized void pastikanKolomStatus(Connection conn) throws SQLException {
        if (kolomStatusSiap) {
            return;
        }
        String cek = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() "
                + "AND TABLE_NAME = 'jadwal_tayang' AND COLUMN_NAME = 'status_jadwal'";
        try (PreparedStatement ps = conn.prepareStatement(cek); ResultSet rs = ps.executeQuery()) {
            rs.next();
            if (rs.getInt(1) == 0) {
                try (PreparedStatement alter = conn.prepareStatement(
                        "ALTER TABLE jadwal_tayang ADD COLUMN status_jadwal VARCHAR(20) NOT NULL DEFAULT 'AKTIF'")) {
                    alter.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Kolom status_jadwal belum ada dan gagal dibuat otomatis. Jalankan di MySQL: "
                    + "ALTER TABLE jadwal_tayang ADD COLUMN status_jadwal VARCHAR(20) NOT NULL DEFAULT 'AKTIF'; ("
                    + e.getMessage() + ")", e);
        }
        kolomStatusSiap = true;
    }

    public List<JadwalTayang> getAll() throws SQLException {
        List<JadwalTayang> daftar = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_tayang ORDER BY waktu_mulai";

        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    public List<JadwalTayang> getByFilm(int idFilm) throws SQLException {
        List<JadwalTayang> daftar = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_tayang WHERE id_film = ? AND waktu_mulai >= NOW() AND status_jadwal = 'AKTIF' ORDER BY waktu_mulai";

        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFilm);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(mapRow(rs));
                }
            }
        }
        return daftar;
    }

    public JadwalTayang getById(int idJadwal) throws SQLException {
        String sql = "SELECT * FROM jadwal_tayang WHERE id_jadwal = ?";

        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJadwal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void tambah(JadwalTayang jadwal) throws SQLException {
        String sql = "INSERT INTO jadwal_tayang (id_film, id_studio, waktu_mulai, harga_tiket, id_admin, status_jadwal) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jadwal.getIdFilm());
            ps.setInt(2, jadwal.getIdStudio());
            ps.setObject(3, jadwal.getWaktuMulai());
            ps.setDouble(4, jadwal.getHargaTiket());
            ps.setInt(5, jadwal.getIdAdmin());
            ps.setString(6, status(jadwal).name());
            ps.executeUpdate();
        }
    }

    public void ubah(JadwalTayang jadwal) throws SQLException {
        String sql = "UPDATE jadwal_tayang SET id_film = ?, id_studio = ?, waktu_mulai = ?, harga_tiket = ?, status_jadwal = ? WHERE id_jadwal = ?";

        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jadwal.getIdFilm());
            ps.setInt(2, jadwal.getIdStudio());
            ps.setObject(3, jadwal.getWaktuMulai());
            ps.setDouble(4, jadwal.getHargaTiket());
            ps.setString(5, status(jadwal).name());
            ps.setInt(6, jadwal.getIdJadwal());
            ps.executeUpdate();
        }
    }

    public void hapus(int idJadwal) throws SQLException {
        String sql = "DELETE FROM jadwal_tayang WHERE id_jadwal = ?";

        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJadwal);
            ps.executeUpdate();
        }
    }

    private JadwalTayang mapRow(ResultSet rs) throws SQLException {
        JadwalTayang jadwal = new JadwalTayang();
        jadwal.setIdJadwal(rs.getInt("id_jadwal"));
        jadwal.setIdFilm(rs.getInt("id_film"));
        jadwal.setIdStudio(rs.getInt("id_studio"));
        jadwal.setWaktuMulai(rs.getObject("waktu_mulai", LocalDateTime.class));
        jadwal.setHargaTiket(rs.getInt("harga_tiket"));
        jadwal.setStatusJadwal(bacaStatus(rs.getString("status_jadwal")));
        jadwal.setIdAdmin(rs.getInt("id_admin"));
        return jadwal;
    }

    private static StatusJadwal status(JadwalTayang jadwal) {
        return jadwal.getStatusJadwal() == null ? StatusJadwal.AKTIF : jadwal.getStatusJadwal();
    }

    private static StatusJadwal bacaStatus(String nilai) {
        if (nilai == null) {
            return StatusJadwal.AKTIF;
        }
        try {
            return StatusJadwal.valueOf(nilai.trim().toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return StatusJadwal.AKTIF;
        }
    }
}
