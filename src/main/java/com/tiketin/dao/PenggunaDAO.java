package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.model.Admin;
import com.tiketin.model.Kasir;
import com.tiketin.model.Pengguna;
import com.tiketin.util.PasswordUtil;

public class PenggunaDAO {

    private static final String SELECT_SEMUA = "SELECT p.id_pengguna, p.username, p.password, p.nama, a.id_admin, k.id_kasir FROM pengguna p LEFT JOIN admin a ON a.id_pengguna = p.id_pengguna LEFT JOIN kasir k ON k.id_pengguna = p.id_pengguna";

    public Pengguna login(String username, String password) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SEMUA + " WHERE p.username = ? AND p.password = ?")) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private Pengguna map(ResultSet rs) throws SQLException {
        int idPengguna = rs.getInt("id_pengguna");
        String user = rs.getString("username");
        String pass = rs.getString("password");
        String nama = rs.getString("nama");
        int idAdmin = rs.getInt("id_admin");
        if (!rs.wasNull()) return new Admin(idAdmin, idPengguna, user, pass, nama);
        int idKasir = rs.getInt("id_kasir");
        if (!rs.wasNull()) return new Kasir(idKasir, idPengguna, user, pass, nama);
        return null;
    }

    public List<Pengguna> getAll() throws SQLException {
        List<Pengguna> hasil = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SEMUA);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pengguna p = map(rs);
                if (p != null) hasil.add(p);
            }
        }
        return hasil;
    }

    public Pengguna getByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SEMUA + " WHERE p.username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void tambahKasir(Kasir kasir) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idPengguna;
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO pengguna (username, password, nama) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, kasir.getUsername());
                    ps.setString(2, kasir.getPassword());
                    ps.setString(3, kasir.getNama());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); idPengguna = rs.getInt(1); }
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO kasir (id_pengguna) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idPengguna);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); kasir.setIdKasir(rs.getInt(1)); }
                }
                kasir.setIdPengguna(idPengguna);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void ubahKasir(Kasir kasir) throws SQLException {
        String sql = "UPDATE pengguna SET username = ?, nama = ?, password = ? WHERE id_pengguna = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kasir.getUsername());
            ps.setString(2, kasir.getNama());
            ps.setString(3, kasir.getPassword());
            ps.setInt(4, kasir.getIdPengguna());
            ps.executeUpdate();
        }
    }

    public void hapusKasir(int idKasir) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idPengguna;
                try (PreparedStatement ps = conn.prepareStatement("SELECT id_pengguna FROM kasir WHERE id_kasir = ?")) {
                    ps.setInt(1, idKasir);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Kasir tidak ditemukan.");
                        idPengguna = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM kasir WHERE id_kasir = ?")) {
                    ps.setInt(1, idKasir);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM pengguna WHERE id_pengguna = ?")) {
                    ps.setInt(1, idPengguna);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}