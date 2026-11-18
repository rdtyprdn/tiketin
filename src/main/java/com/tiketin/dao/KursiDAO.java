package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.model.Kursi;

public class KursiDAO {

    public List<Kursi> getAll() throws SQLException {
        List<Kursi> daftar = new ArrayList<>();
        String sql = "SELECT * FROM kursi ORDER BY id_studio, nomor_kursi";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    public List<Kursi> getByStudio(int idStudio) throws SQLException {
        List<Kursi> daftar = new ArrayList<>();
        String sql = "SELECT * FROM kursi WHERE id_studio = ? ORDER BY nomor_kursi";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idStudio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(mapRow(rs));
                }
            }
        }
        return daftar;
    }

    public Kursi getById(int idKursi) throws SQLException {
        String sql = "SELECT * FROM kursi WHERE id_kursi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idKursi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void tambah(Kursi kursi) throws SQLException {
        String sql = "INSERT INTO kursi (id_studio, nomor_kursi, tipe_kursi, id_admin) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, kursi.getIdStudio());
            ps.setString(2, kursi.getNomorKursi());
            ps.setString(3, kursi.getTipeKursi());
            ps.setInt(4, kursi.getIdAdmin());
            ps.executeUpdate();
        }
    }

    public void ubah(Kursi kursi) throws SQLException {
        String sql = "UPDATE kursi SET id_studio = ?, nomor_kursi = ?, tipe_kursi = ? WHERE id_kursi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, kursi.getIdStudio());
            ps.setString(2, kursi.getNomorKursi());
            ps.setString(3, kursi.getTipeKursi());
            ps.setInt(4, kursi.getIdKursi());
            ps.executeUpdate();
        }
    }

    public void hapus(int idKursi) throws SQLException {
        String sql = "DELETE FROM kursi WHERE id_kursi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idKursi);
            ps.executeUpdate();
        }
    }

    private Kursi mapRow(ResultSet rs) throws SQLException {
        Kursi kursi = new Kursi();
        kursi.setIdKursi(rs.getInt("id_kursi"));
        kursi.setIdStudio(rs.getInt("id_studio"));
        kursi.setNomorKursi(rs.getString("nomor_kursi"));
        kursi.setTipeKursi(rs.getString("tipe_kursi"));
        kursi.setIdAdmin(rs.getInt("id_admin"));
        return kursi;
    }
}