package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.tiketin.config.DatabaseConnection;

public class DetailPemesananDAO {

    public Set<Integer> getKursiTerisi(int idJadwal) throws SQLException {
        Set<Integer> terisi = new HashSet<>();
        String sql = "SELECT d.id_kursi FROM detail_pemesanan d "
                + "JOIN pemesanan p ON p.id_pemesanan = d.id_pemesanan "
                + "WHERE d.id_jadwal = ? AND p.status_pemesanan <> 'DIBATALKAN'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJadwal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    terisi.add(rs.getInt("id_kursi"));
                }
            }
        }
        return terisi;
    }

    public boolean isKursiTerisi(int idJadwal, int idKursi) throws SQLException {
        String sql = "SELECT 1 FROM detail_pemesanan d "
                + "JOIN pemesanan p ON p.id_pemesanan = d.id_pemesanan "
                + "WHERE d.id_jadwal = ? AND d.id_kursi = ? AND p.status_pemesanan <> 'DIBATALKAN'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJadwal);
            ps.setInt(2, idKursi);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}