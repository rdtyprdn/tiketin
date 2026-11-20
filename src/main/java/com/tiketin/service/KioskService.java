package com.tiketin.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiketin.dao.PemesananDAO;
import com.tiketin.enums.KanalPemesanan;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.KasirTiketItem;
import com.tiketin.model.Kursi;
import com.tiketin.payment.PembayaranQRIS;

// Simpan pesanan dari kiosk. Proses intinya sama dengan kasir (PemesananService),
// bedanya kanal KIOSK, tanpa kasir, dan metode bayar QRIS.
public class KioskService {

    private final PemesananService pemesananService = new PemesananService();
    private final PemesananDAO pemesananDAO = new PemesananDAO();

    // Hasilnya id pemesanan. PemesananService otomatis membuat pelanggan anonim untuk kanal KIOSK.
    public int bayarQris(JadwalTayang jadwal, List<Kursi> kursi) {
        List<Integer> idKursi = new ArrayList<>();
        for (Kursi k : kursi) {
            idKursi.add(k.getIdKursi());
        }
        try {
            return pemesananService.buatPemesanan(jadwal.getIdJadwal(), idKursi, KanalPemesanan.KIOSK,
                    null, new PembayaranQRIS());
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Kode tiket (TKT-xxxx) dan nomor kursi untuk halaman e-ticket
    public List<KasirTiketItem> tiket(int idPemesanan) {
        try {
            return pemesananDAO.getTiketPemesanan(idPemesanan);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memuat tiket: " + e.getMessage());
        }
    }
}
