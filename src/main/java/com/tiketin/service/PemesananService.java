package com.tiketin.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusPemesanan;
import com.tiketin.model.DataPembayaran;
import com.tiketin.payment.Pembayaran;

public class PemesananService {

    public int buatPemesanan(int idJadwal, List<Integer> daftarIdKursi, KanalPemesanan kanal,
            Integer idKasir, Pembayaran pembayaran) throws SQLException {

        if (daftarIdKursi == null || daftarIdKursi.isEmpty()) {
            throw new IllegalArgumentException("Pilih minimal satu kursi.");
        }

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Ambil harga tiket dari jadwal
            double harga = ambilHarga(conn, idJadwal);
            double total = harga * daftarIdKursi.size();

            // 2. Kiosk: buat pelanggan anonim. Kasir: pakai idKasir
            Integer idPelanggan = null;
            if (kanal == KanalPemesanan.KIOSK) {
                idPelanggan = buatPelanggan(conn);
                idKasir = null;
            }

            // 3. Simpan pemesanan
            int idPemesanan = simpanPemesanan(conn, idJadwal, idKasir, idPelanggan, kanal, total);

            // 4. Simpan detail kursi + tiket
            for (int idKursi : daftarIdKursi) {
                int idDetail = simpanDetail(conn, idPemesanan, idJadwal, idKursi, harga);
                simpanTiket(conn, idDetail);
            }

            // 5. Proses pembayaran (Tunai bisa throw kalau uang kurang)
            DataPembayaran data = pembayaran.proses(idPemesanan, total);
            simpanPembayaran(conn, data);

            // 6. Tandai lunas
            ubahStatus(conn, idPemesanan, StatusPemesanan.BERHASIL);

            conn.commit();
            return idPemesanan;

        } catch (SQLIntegrityConstraintViolationException e) {
            conn.rollback();
            throw new SQLException("Ada kursi yang sudah dipesan orang lain. Silakan pilih ulang.", e);
        } catch (SQLException | RuntimeException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void batalkan(int idPemesanan) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE t FROM tiket t JOIN detail_pemesanan d ON d.id_detail = t.id_detail WHERE d.id_pemesanan = ?")) {
                ps.setInt(1, idPemesanan);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM detail_pemesanan WHERE id_pemesanan = ?")) {
                ps.setInt(1, idPemesanan);
                ps.executeUpdate();
            }
            ubahStatus(conn, idPemesanan, StatusPemesanan.DIBATALKAN);

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // ===== method bantu =====

    private double ambilHarga(Connection conn, int idJadwal) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT harga_tiket FROM jadwal_tayang WHERE id_jadwal = ?")) {
            ps.setInt(1, idJadwal);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Jadwal tidak ditemukan.");
                }
                return rs.getDouble("harga_tiket");
            }
        }
    }

    private int buatPelanggan(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pelanggan () VALUES ()", Statement.RETURN_GENERATED_KEYS)) {
            ps.executeUpdate();
            return ambilIdBaru(ps);
        }
    }

    private int simpanPemesanan(Connection conn, int idJadwal, Integer idKasir, Integer idPelanggan,
            KanalPemesanan kanal, double total) throws SQLException {
        String sql = "INSERT INTO pemesanan (id_jadwal, id_kasir, id_pelanggan, kanal_pemesanan, status_pemesanan, total_bayar) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idJadwal);
            ps.setObject(2, idKasir);
            ps.setObject(3, idPelanggan);
            ps.setString(4, kanal.name());
            ps.setString(5, StatusPemesanan.MENUNGGU_PEMBAYARAN.name());
            ps.setDouble(6, total);
            ps.executeUpdate();
            return ambilIdBaru(ps);
        }
    }

    private int simpanDetail(Connection conn, int idPemesanan, int idJadwal, int idKursi, double harga)
            throws SQLException {
        String sql = "INSERT INTO detail_pemesanan (id_pemesanan, id_jadwal, id_kursi, harga_tiket) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idPemesanan);
            ps.setInt(2, idJadwal);
            ps.setInt(3, idKursi);
            ps.setDouble(4, harga);
            ps.executeUpdate();
            return ambilIdBaru(ps);
        }
    }

    private void simpanTiket(Connection conn, int idDetail) throws SQLException {
        String kode = String.format("TKT-%04d", idDetail);
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO tiket (id_detail, kode_tiket, tanggal_terbit) VALUES (?, ?, NOW())")) {
            ps.setInt(1, idDetail);
            ps.setString(2, kode);
            ps.executeUpdate();
        }
    }

    private void simpanPembayaran(Connection conn, DataPembayaran data) throws SQLException {
        String sql = "INSERT INTO pembayaran (id_pemesanan, metode_pembayaran, status_pembayaran, jumlah_bayar, "
                + "jumlah_uang_diterima, kembalian) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, data.getIdPemesanan());
            ps.setString(2, data.getMetodePembayaran());
            ps.setString(3, data.getStatusPembayaran().name());
            ps.setDouble(4, data.getJumlahBayar());
            ps.setObject(5, data.getJumlahUangDiterima());
            ps.setObject(6, data.getKembalian());
            ps.executeUpdate();
        }
    }

    private void ubahStatus(Connection conn, int idPemesanan, StatusPemesanan status) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE pemesanan SET status_pemesanan = ? WHERE id_pemesanan = ?")) {
            ps.setString(1, status.name());
            ps.setInt(2, idPemesanan);
            ps.executeUpdate();
        }
    }

    private int ambilIdBaru(PreparedStatement ps) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            keys.next();
            return keys.getInt(1);
        }
    }
}