package com.tiketin.service;

import com.tiketin.enums.KanalPemesanan;
import com.tiketin.enums.StatusJadwal;
import com.tiketin.enums.StatusPemesanan;
import com.tiketin.model.BarisTransaksi;
import com.tiketin.model.RingkasanDashboard;
import com.tiketin.model.RingkasanLaporan;
import com.tiketin.model.TitikGrafik;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaporanService {
    private static final DateTimeFormatter HARI =
            DateTimeFormatter.ofPattern("dd/MM", Locale.of("id", "ID"));

    private final PenyimpananSementara data = PenyimpananSementara.get();
    private final TransaksiService transaksiService = new TransaksiService();

    public RingkasanDashboard dashboard() {
        int film = data.film().size();
        int jadwalAktif = (int) data.jadwal().stream()
                .filter(jadwal -> jadwal.getStatusJadwal() == StatusJadwal.AKTIF)
                .count();
        List<BarisTransaksi> semua = transaksiService.semua();
        int pendapatan = semua.stream()
                .filter(baris -> baris.getStatus() == StatusPemesanan.BERHASIL)
                .mapToInt(BarisTransaksi::getTotalBayar)
                .sum();
        return new RingkasanDashboard(film, jadwalAktif, semua.size(), pendapatan, grafikTujuhHari(semua));
    }

    public RingkasanLaporan laporan(LocalDate dari, LocalDate sampai) {
        if (dari == null || sampai == null) {
            throw new IllegalArgumentException("Periode wajib diisi.");
        }
        if (sampai.isBefore(dari)) {
            throw new IllegalArgumentException("Tanggal akhir tidak boleh sebelum tanggal awal.");
        }
        List<BarisTransaksi> rekap = transaksiService.semua().stream()
                .filter(baris -> {
                    LocalDate tanggal = baris.getWaktu().toLocalDate();
                    return !tanggal.isBefore(dari) && !tanggal.isAfter(sampai);
                })
                .toList();
        int penjualan = 0;
        int tiket = 0;
        int berhasil = 0;
        int kiosk = 0;
        int loket = 0;
        for (BarisTransaksi baris : rekap) {
            if (baris.getStatus() != StatusPemesanan.BERHASIL) {
                continue;
            }
            berhasil++;
            penjualan += baris.getTotalBayar();
            tiket += baris.getJumlahKursi();
            if (baris.getKanal() == KanalPemesanan.KIOSK) {
                kiosk += baris.getTotalBayar();
            } else {
                loket += baris.getTotalBayar();
            }
        }
        return new RingkasanLaporan(penjualan, tiket, berhasil, kiosk, loket, rekap);
    }

    private List<TitikGrafik> grafikTujuhHari(List<BarisTransaksi> semua) {
        List<TitikGrafik> titik = new ArrayList<>();
        LocalDate hariIni = LocalDate.now();
        for (int mundur = 6; mundur >= 0; mundur--) {
            LocalDate hari = hariIni.minusDays(mundur);
            List<BarisTransaksi> berhasil = semua.stream()
                    .filter(baris -> baris.getStatus() == StatusPemesanan.BERHASIL)
                    .filter(baris -> baris.getWaktu().toLocalDate().equals(hari))
                    .toList();
            int nilai = berhasil.stream().mapToInt(BarisTransaksi::getTotalBayar).sum();
            titik.add(new TitikGrafik(hari.format(HARI), nilai, berhasil.size()));
        }
        return titik;
    }
}
