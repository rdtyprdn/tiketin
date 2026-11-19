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

    public List<JadwalTayang> getAll() throws SQLException {
        List<JadwalTayang> daftar = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_tayang ORDER BY waktu_mulai";

        try (Connection conn = DatabaseConnection.getConnection();
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
        String sql = "SELECT * FROM jadwal_tayang WHERE id_film = ? AND waktu_mulai >= NOW() ORDER BY waktu_mulai";

        try (Connection conn = DatabaseConnection.getConnection();
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

        try (Connection conn = DatabaseConnection.getConnection();
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
        String sql = "INSERT INTO jadwal_tayang (id_film, id_studio, waktu_mulai, harga_tiket, id_admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jadwal.getIdFilm());
            ps.setInt(2, jadwal.getIdStudio());
            ps.setObject(3, jadwal.getWaktuMulai());
            ps.setDouble(4, jadwal.getHargaTiket());
            ps.setInt(5, jadwal.getIdAdmin());
            ps.executeUpdate();
        }
    }

    public void ubah(JadwalTayang jadwal) throws SQLException {
        String sql = "UPDATE jadwal_tayang SET id_film = ?, id_studio = ?, waktu_mulai = ?, harga_tiket = ? WHERE id_jadwal = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jadwal.getIdFilm());
            ps.setInt(2, jadwal.getIdStudio());
            ps.setObject(3, jadwal.getWaktuMulai());
            ps.setDouble(4, jadwal.getHargaTiket());
            ps.setInt(5, jadwal.getIdJadwal());
            ps.executeUpdate();
        }
    }

    public void hapus(int idJadwal) throws SQLException {
        String sql = "DELETE FROM jadwal_tayang WHERE id_jadwal = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
        jadwal.setStatusJadwal(StatusJadwal.AKTIF);
        jadwal.setIdAdmin(rs.getInt("id_admin"));
        return jadwal;
    }
}