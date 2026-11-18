package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.model.Studio;

public class StudioDAO {

    public List<Studio> getAll() throws SQLException {
        List<Studio> daftar = new ArrayList<>();
        String sql = "SELECT * FROM studio ORDER BY nama_studio";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    public Studio getById(int idStudio) throws SQLException {
        String sql = "SELECT * FROM studio WHERE id_studio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idStudio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void tambah(Studio studio) throws SQLException {
        String sql = "INSERT INTO studio (nama_studio, id_admin) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studio.getNamaStudio());
            ps.setInt(2, studio.getIdAdmin());
            ps.executeUpdate();
        }
    }

    public void ubah(Studio studio) throws SQLException {
        String sql = "UPDATE studio SET nama_studio = ? WHERE id_studio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studio.getNamaStudio());
            ps.setInt(2, studio.getIdStudio());
            ps.executeUpdate();
        }
    }

    public void hapus(int idStudio) throws SQLException {
        String sql = "DELETE FROM studio WHERE id_studio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idStudio);
            ps.executeUpdate();
        }
    }

    private Studio mapRow(ResultSet rs) throws SQLException {
        Studio studio = new Studio();
        studio.setNamaStudio(rs.getString("nama_studio"));
        studio.setIdStudio(rs.getInt("id_studio"));
        studio.setIdAdmin(rs.getInt("id_admin"));
        return studio;
    }
}