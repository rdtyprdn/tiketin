package com.tiketin.service;

import com.tiketin.model.Kursi;
import com.tiketin.model.Studio;

import java.util.List;
import java.util.Locale;

public class KursiService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public List<Kursi> cari(Integer idStudio, String kata) {
        String kueri = kata == null ? "" : kata.trim().toLowerCase(Locale.ROOT);
        return data.kursi().stream()
                .filter(kursi -> idStudio == null || kursi.getIdStudio() == idStudio)
                .filter(kursi -> kueri.isEmpty()
                        || kursi.getNomorKursi().toLowerCase(Locale.ROOT).contains(kueri)
                        || kursi.getTipeKursi().toLowerCase(Locale.ROOT).contains(kueri))
                .toList();
    }

    public Kursi tambah(int idStudio, String nomor, String tipe, int idAdmin) {
        return data.simpanKursi(susun(0, idStudio, nomor, tipe, idAdmin));
    }

    public Kursi ubah(int idKursi, int idStudio, String nomor, String tipe) {
        Kursi lama = wajibAda(idKursi);
        return data.simpanKursi(susun(idKursi, idStudio, nomor, tipe, lama.getIdAdmin()));
    }

    public void hapus(int idKursi) {
        wajibAda(idKursi);
        boolean dipakai = data.detail().stream().anyMatch(detail -> detail.getIdKursi() == idKursi);
        if (dipakai) {
            throw new IllegalStateException("Kursi tidak bisa dihapus karena sudah tercatat pada pemesanan.");
        }
        data.hapusKursi(idKursi);
    }

    private Kursi susun(int idKursi, int idStudio, String nomor, String tipe, int idAdmin) {
        if (data.cariStudio(idStudio) == null) {
            throw new IllegalArgumentException("Studio wajib dipilih.");
        }
        String nomorBersih = Validasi.wajib(nomor, "Nomor kursi wajib diisi.").toUpperCase(Locale.ROOT);
        String tipeBersih = Validasi.wajib(tipe, "Tipe kursi wajib diisi.");
        if (nomorBersih.length() > 10) {
            throw new IllegalArgumentException("Nomor kursi maksimal 10 karakter.");
        }
        boolean bentrok = data.kursi().stream().anyMatch(kursi ->
                kursi.getIdKursi() != idKursi
                        && kursi.getIdStudio() == idStudio
                        && kursi.getNomorKursi().equalsIgnoreCase(nomorBersih));
        if (bentrok) {
            throw new IllegalArgumentException("Nomor kursi sudah ada di studio ini.");
        }
        return new Kursi(idKursi, idStudio, nomorBersih, tipeBersih, idAdmin);
    }

    private Kursi wajibAda(int idKursi) {
        Kursi kursi = data.cariKursi(idKursi);
        if (kursi == null) {
            throw new IllegalArgumentException("Kursi tidak ditemukan.");
        }
        return kursi;
    }

    public List<Studio> studio() {
        return data.studio();
    }
}
