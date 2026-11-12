package com.tiketin.payment;

import com.tiketin.enums.StatusPembayaran;
import com.tiketin.model.DataPembayaran;

public class PembayaranTunai implements Pembayaran {

    private double uangDiterima;
    
    public PembayaranTunai(double uangDiterima){
        this.uangDiterima = uangDiterima;
    }

    @Override
    public String getMetode(){
        return "TUNAI";
    }

    @Override 
    public DataPembayaran proses(int idPemesanan, double jumlahBayar){
        if (uangDiterima < jumlahBayar){
            throw new IllegalArgumentException("Uang yang diterima kurang dari total bayar.");
        }

        DataPembayaran data = new DataPembayaran();
        data.setIdPemesanan(idPemesanan);
        data.setMetodePembayaran(getMetode());
        data.setStatusPembayaran(StatusPembayaran.BERHASIL);
        data.setJumlahBayar(jumlahBayar);
        data.setJumlahUangDiterima(uangDiterima);
        data.setKembalian(uangDiterima - jumlahBayar);
        return data;
    }
}
