package com.tiketin.service;

import com.tiketin.model.BarisTransaksi;
import com.tiketin.model.DetailPemesanan;
import com.tiketin.model.JadwalTayang;
import com.tiketin.model.Kasir;
import com.tiketin.model.Kursi;
import com.tiketin.model.Pemesanan;
import com.tiketin.util.Waktu;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TransaksiService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public List<BarisTransaksi> cari(String kata) {
        String kueri = kata == null ? "" : kata.trim().toLowerCase(Locale.ROOT);
        return semua().stream()
                .filter(baris -> kueri.isEmpty()
                        || String.valueOf(baris.getIdPemesanan()).contains(kueri)
                        || baris.getJudulFilm().toLowerCase(Locale.ROOT).contains(kueri)
                        || baris.getKanal().getLabel().toLowerCase(Locale.ROOT).contains(kueri))
                .toList();
    }

    public List<BarisTransaksi> semua() {
        return data.pemesanan().stream()
                .map(this::keBaris)
                .sorted(Comparator.comparing(BarisTransaksi::getWaktu).reversed())
                .toList();
    }

    private BarisTransaksi keBaris(Pemesanan pesanan) {
        JadwalTayang jadwal = data.cariJadwal(pesanan.getIdJadwal());
        String judul = jadwal == null ? "-" : namaFilm(jadwal.getIdFilm());
        String studio = jadwal == null ? "-" : namaStudio(jadwal.getIdStudio());
        String tayang = jadwal == null ? "-" : Waktu.format(jadwal.getWaktuMulai());
        String kasir = "Tidak ada";
        if (pesanan.getIdKasir() != null) {
            Kasir petugas = data.cariKasir(pesanan.getIdKasir());
            kasir = petugas == null ? "-" : petugas.getNama();
        }
        List<String> nomor = data.detail().stream()
                .filter(detail -> detail.getIdPemesanan() == pesanan.getIdPemesanan())
                .map(DetailPemesanan::getIdKursi)
                .map(data::cariKursi)
                .filter(kursi -> kursi != null)
                .map(Kursi::getNomorKursi)
                .sorted()
                .toList();
        String kursi = nomor.isEmpty() ? "-" : nomor.stream().collect(Collectors.joining(", "));
        return new BarisTransaksi(pesanan.getIdPemesanan(), pesanan.getIdJadwal(), pesanan.getWaktuPemesanan(),
                pesanan.getKanalPemesanan(), kasir, pesanan.getTotalBayar(), pesanan.getStatusPemesanan(),
                judul, studio, tayang, kursi, nomor.size());
    }

    private String namaFilm(int idFilm) {
        return data.cariFilm(idFilm) == null ? "-" : data.cariFilm(idFilm).getJudul();
    }

    private String namaStudio(int idStudio) {
        return data.cariStudio(idStudio) == null ? "-" : data.cariStudio(idStudio).getNamaStudio();
    }
}
