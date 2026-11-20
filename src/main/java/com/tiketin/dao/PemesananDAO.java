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
import com.tiketin.model.KasirRiwayatItem;
import com.tiketin.model.KasirTiketItem;
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

    // Riwayat transaksi milik satu kasir, terbaru di atas
    public List<KasirRiwayatItem> getRiwayatKasir(int idKasir) throws SQLException {
        List<KasirRiwayatItem> daftar = new ArrayList<>();
        String sql = "SELECT p.id_pemesanan, p.waktu_pemesanan, p.total_bayar, p.status_pemesanan, f.judul, "
                + "(SELECT COUNT(*) FROM detail_pemesanan d WHERE d.id_pemesanan = p.id_pemesanan) AS jumlah_tiket "
                + "FROM pemesanan p "
                + "JOIN jadwal_tayang j ON j.id_jadwal = p.id_jadwal "
                + "JOIN film f ON f.id_film = j.id_film "
                + "WHERE p.id_kasir = ? "
                + "ORDER BY p.waktu_pemesanan DESC, p.id_pemesanan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKasir);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(new KasirRiwayatItem(
                            kodeTransaksi(rs.getInt("id_pemesanan")),
                            rs.getTimestamp("waktu_pemesanan").toLocalDateTime(),
                            rs.getString("judul"),
                            rs.getInt("jumlah_tiket"),
                            (int) rs.getDouble("total_bayar"),
                            labelStatus(rs.getString("status_pemesanan"))));
                }
            }
        }
        return daftar;
    }

    // Daftar tiket (kursi, kode, harga) dari satu pemesanan
    public List<KasirTiketItem> getTiketPemesanan(int idPemesanan) throws SQLException {
        List<KasirTiketItem> daftar = new ArrayList<>();
        String sql = "SELECT k.nomor_kursi, t.kode_tiket, d.harga_tiket "
                + "FROM detail_pemesanan d "
                + "JOIN kursi k ON k.id_kursi = d.id_kursi "
                + "JOIN tiket t ON t.id_detail = d.id_detail "
                + "WHERE d.id_pemesanan = ? "
                + "ORDER BY LEFT(k.nomor_kursi, 1), CAST(SUBSTRING(k.nomor_kursi, 2) AS UNSIGNED)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPemesanan);
            try (ResultSet rs = ps.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    daftar.add(new KasirTiketItem(
                            no++,
                            rs.getString("nomor_kursi"),
                            rs.getString("kode_tiket"),
                            (int) rs.getDouble("harga_tiket")));
                }
            }
        }
        return daftar;
    }

    // Contoh: 7 -> TRX-0007
    public static String kodeTransaksi(int idPemesanan) {
        return String.format("TRX-%04d", idPemesanan);
    }

    private String labelStatus(String status) {
        if (StatusPemesanan.LUNAS.name().equals(status)) {
            return "Lunas";
        }
        if (StatusPemesanan.DIBATALKAN.name().equals(status)) {
            return "Dibatalkan";
        }
        return "Menunggu";
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
