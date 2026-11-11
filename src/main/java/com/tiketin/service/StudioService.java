package com.tiketin.service;

import com.tiketin.model.Studio;

import java.util.List;
import java.util.Locale;

public class StudioService {
    private final PenyimpananSementara data = PenyimpananSementara.get();

    public List<Studio> cari(String kata) {
        String kueri = kata == null ? "" : kata.trim().toLowerCase(Locale.ROOT);
        return data.studio().stream()
                .filter(studio -> kueri.isEmpty()
                        || studio.getNamaStudio().toLowerCase(Locale.ROOT).contains(kueri))
                .toList();
    }

    public Studio tambah(String nama, int idAdmin) {
        return data.simpanStudio(susun(0, nama, idAdmin));
    }

    public Studio ubah(int idStudio, String nama) {
        Studio lama = wajibAda(idStudio);
        return data.simpanStudio(susun(idStudio, nama, lama.getIdAdmin()));
    }

    public void hapus(int idStudio) {
        wajibAda(idStudio);
        boolean adaKursi = data.kursi().stream().anyMatch(kursi -> kursi.getIdStudio() == idStudio);
        boolean adaJadwal = data.jadwal().stream().anyMatch(jadwal -> jadwal.getIdStudio() == idStudio);
        if (adaKursi || adaJadwal) {
            throw new IllegalStateException("Studio tidak bisa dihapus karena masih memiliki kursi atau jadwal.");
        }
        data.hapusStudio(idStudio);
    }

    public String nama(int idStudio) {
        Studio studio = data.cariStudio(idStudio);
        return studio == null ? "-" : studio.getNamaStudio();
    }

    private Studio susun(int idStudio, String nama, int idAdmin) {
        String namaBersih = Validasi.wajib(nama, "Nama studio wajib diisi.");
        if (namaBersih.length() > 40) {
            throw new IllegalArgumentException("Nama studio maksimal 40 karakter.");
        }
        boolean bentrok = data.studio().stream().anyMatch(studio ->
                studio.getIdStudio() != idStudio
                        && studio.getNamaStudio().equalsIgnoreCase(namaBersih));
        if (bentrok) {
            throw new IllegalArgumentException("Nama studio sudah dipakai.");
        }
        return new Studio(idStudio, namaBersih, idAdmin);
    }

    private Studio wajibAda(int idStudio) {
        Studio studio = data.cariStudio(idStudio);
        if (studio == null) {
            throw new IllegalArgumentException("Studio tidak ditemukan.");
        }
        return studio;
    }
}
