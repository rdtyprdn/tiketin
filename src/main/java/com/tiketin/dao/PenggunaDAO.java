package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.model.Admin;
import com.tiketin.model.Kasir;
import com.tiketin.model.Pengguna;
import com.tiketin.util.PasswordUtil;


public class PenggunaDAO {
    
    public Pengguna login(String username, String password) throws SQLException {
        String sql = "SELECT p.id_pengguna, p.username, p.password, p.nama, " + "a.id_admin, k.id_kasir " + "FROM pengguna p " + "LEFT JOIN admin a ON a.id_pengguna = p.id_pengguna " + "LEFT JOIN kasir k ON k.id_pengguna = p.id_pengguna " + "WHERE p.username = ? AND p.password = ?";

    try (Connection conn = DatabaseConnection.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, PasswordUtil.hash(password));
    
    try (ResultSet rs = ps.executeQuery()){
        if (!rs.next()) {
            return null;
        }

        int idPengguna = rs.getInt("id_pengguna");
        String user = rs.getString("username");
        String pass = rs.getString("password");
        String nama = rs.getString("nama");

        int idAdmin = rs.getInt("id_admin");
        if (!rs.wasNull()) {
            return new Admin(idAdmin, idPengguna, user, pass, nama);
        }

        int idKasir = rs.getInt("id_kasir");
        if (!rs.wasNull()) {
            return new Kasir(idKasir, idPengguna, user, pass, nama);
        }
        return null;
    }
    }

    }
}
