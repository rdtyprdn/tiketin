package com.tiketin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.config.DatabaseConnection;
import com.tiketin.model.Film;

public class FilmDAO {

    // Kolom detail film (sutradara, tahun_rilis, rating_usia, rating) ditambahkan belakangan.
    // Database lama yang belum punya kolom ini dilengkapi otomatis sekali saat aplikasi
    // pertama kali memakai tabel film (database baru dari tiketin.sql sudah lengkap).
    private static final String[][] KOLOM_DETAIL = {
        { "sutradara", "VARCHAR(150) NULL" },
        { "tahun_rilis", "SMALLINT NULL" },
        { "rating_usia", "VARCHAR(10) NOT NULL DEFAULT 'SU'" },
        { "rating", "DECIMAL(2,1) NULL" },
    };
    private static volatile boolean kolomDetailSiap = false;

    private static Connection koneksi() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (!kolomDetailSiap) {
            pastikanKolomDetail(conn);
        }
        return conn;
    }

    private static synchronized void pastikanKolomDetail(Connection conn) throws SQLException {
        if (kolomDetailSiap) {
            return;
        }
        String cek = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() "
                + "AND TABLE_NAME = 'film' AND COLUMN_NAME = ?";
        for (String[] kolom : KOLOM_DETAIL) {
            String sql = "ALTER TABLE film ADD COLUMN " + kolom[0] + " " + kolom[1];
            try (PreparedStatement ps = conn.prepareStatement(cek)) {
                ps.setString(1, kolom[0]);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        continue;
                    }
                }
                try (PreparedStatement alter = conn.prepareStatement(sql)) {
                    alter.executeUpdate();
                }
            } catch (SQLException e) {
                throw new SQLException("Kolom film." + kolom[0] + " belum ada dan gagal dibuat otomatis. "
                        + "Jalankan di MySQL: " + sql + "; (" + e.getMessage() + ")", e);
            }
        }
        kolomDetailSiap = true;
    }
    
    public List<Film> getAll() throws SQLException {
        List<Film> daftar = new ArrayList<>();
        String sql = "SELECT * FROM film ORDER BY judul";

         try (Connection conn = koneksi();
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
        try (Connection conn = koneksi();
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
        String sql = "INSERT INTO film (judul, genre, durasi, sinopsis, id_admin, sutradara, tahun_rilis, rating_usia, rating) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, film.getJudul());
            ps.setString(2, film.getGenre());
            ps.setInt(3, film.getDurasi());
            ps.setString(4, film.getSinopsis());
            ps.setInt(5, film.getIdAdmin());
            isiDetail(ps, 6, film);
            ps.executeUpdate();
        }
    }

    public void ubah(Film film) throws SQLException {
        String sql = "UPDATE film SET judul = ?, genre = ?, durasi = ?, sinopsis = ?, "
                + "sutradara = ?, tahun_rilis = ?, rating_usia = ?, rating = ? WHERE id_film = ?";
        try (Connection conn = koneksi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, film.getJudul());
            ps.setString(2, film.getGenre());
            ps.setInt(3, film.getDurasi());
            ps.setString(4, film.getSinopsis());
            isiDetail(ps, 5, film);
            ps.setInt(9, film.getIdFilm());
            ps.executeUpdate();
        }
    }

    public void hapus(int idFilm) throws SQLException {
        String sql = "DELETE FROM film WHERE id_film = ?";
        try (Connection conn = koneksi();
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
        film.setSutradara(rs.getString("sutradara"));
        int tahun = rs.getInt("tahun_rilis");
        film.setTahunRilis(rs.wasNull() ? null : tahun);
        String usia = rs.getString("rating_usia");
        film.setRatingUsia(usia == null || usia.isBlank() ? "SU" : usia);
        java.math.BigDecimal rating = rs.getBigDecimal("rating");
        film.setRating(rating == null ? null : rating.doubleValue());
        return film;
    }

    // Parameter sutradara, tahun_rilis, rating_usia, rating mulai dari posisi awal
    private static void isiDetail(PreparedStatement ps, int awal, Film film) throws SQLException {
        ps.setString(awal, film.getSutradara());
        if (film.getTahunRilis() == null) {
            ps.setNull(awal + 1, Types.SMALLINT);
        } else {
            ps.setInt(awal + 1, film.getTahunRilis());
        }
        ps.setString(awal + 2, film.getRatingUsia() == null ? "SU" : film.getRatingUsia());
        if (film.getRating() == null) {
            ps.setNull(awal + 3, Types.DECIMAL);
        } else {
            ps.setBigDecimal(awal + 3, java.math.BigDecimal.valueOf(film.getRating()).setScale(1, java.math.RoundingMode.HALF_UP));
        }
    }
}
