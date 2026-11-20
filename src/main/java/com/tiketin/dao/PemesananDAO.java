package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusPemesanan;
import com.tiketin.model.Pemesanan;

public class PemesananDAO {

    public List<Pemesanan> getAll() throws SQLException {
        List<Pemesanan> daftar = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan ORDER BY waktu_pemesanan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    private Pemesanan mapRow(ResultSet rs) throws SQLException {
        return new Pemesanan(
                rs.getInt("id_pemesanan"),
                rs.getInt("id_jadwal"),
                rs.getObject("id_kasir", Integer.class),
                rs.getObject("id_pelanggan", Integer.class),
                rs.getTimestamp("waktu_pemesanan").toLocalDateTime(),
                KanalPemesanan.valueOf(rs.getString("kanal_pemesanan")),
                StatusPemesanan.valueOf(rs.getString("status_pemesanan")),
                rs.getDouble("total_bayar"));
    }
}