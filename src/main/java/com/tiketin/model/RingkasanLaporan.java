package com.tiketin.model;

import java.util.List;

public class RingkasanLaporan {
    private final int totalPenjualan;
    private final int totalTiket;
    private final int jumlahTransaksiBerhasil;
    private final int penjualanKiosk;
    private final int penjualanLoket;
    private final List<BarisTransaksi> rekap;

    public RingkasanLaporan(int totalPenjualan, int totalTiket, int jumlahTransaksiBerhasil,
                            int penjualanKiosk, int penjualanLoket, List<BarisTransaksi> rekap) {
        this.totalPenjualan = totalPenjualan;
        this.totalTiket = totalTiket;
        this.jumlahTransaksiBerhasil = jumlahTransaksiBerhasil;
        this.penjualanKiosk = penjualanKiosk;
        this.penjualanLoket = penjualanLoket;
        this.rekap = List.copyOf(rekap);
    }

    public int getTotalPenjualan() {
        return totalPenjualan;
    }

    public int getTotalTiket() {
        return totalTiket;
    }

    public int getJumlahTransaksiBerhasil() {
        return jumlahTransaksiBerhasil;
    }

    public int getPenjualanKiosk() {
        return penjualanKiosk;
    }

    public int getPenjualanLoket() {
        return penjualanLoket;
    }

    public List<BarisTransaksi> getRekap() {
        return rekap;
    }
}
