package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.model.Film;

public class FilmDAO {
    
    public List<Film> getAll() throws SQLException {
        List<Film> daftar = new ArrayList<>();
        String sql = "SELECT * FROM film ORDER BY judul";

         try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    
    public Film getById(int idFilm) throws SQLException {
        String sql = "SELECT * FROM film WHERE id_film = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFilm);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void tambah(Film film) throws SQLException {
        String sql = "INSERT INTO film (judul, genre, durasi, sinopsis, id_admin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, film.getJudul());
            ps.setString(2, film.getGenre());
            ps.setInt(3, film.getDurasi());
            ps.setString(4, film.getSinopsis());
            ps.setInt(5, film.getIdAdmin());
            ps.executeUpdate();
        }
    }

    public void ubah(Film film) throws SQLException {
        String sql = "UPDATE film SET judul = ?, genre = ?, durasi = ?, sinopsis = ? WHERE id_film = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, film.getJudul());
            ps.setString(2, film.getGenre());
            ps.setInt(3, film.getDurasi());
            ps.setString(4, film.getSinopsis());
            ps.setInt(5, film.getIdFilm());
            ps.executeUpdate();
        }
    }

    public void hapus(int idFilm) throws SQLException {
        String sql = "DELETE FROM film WHERE id_film = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFilm);
            ps.executeUpdate();
        }
    }
    
      private Film mapRow(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setIdFilm(rs.getInt("id_film"));
        film.setJudul(rs.getString("judul"));
        film.setGenre(rs.getString("genre"));
        film.setDurasi(rs.getInt("durasi"));
        film.setSinopsis(rs.getString("sinopsis"));
        film.setIdAdmin(rs.getInt("id_admin"));
        return film;
    }
}
