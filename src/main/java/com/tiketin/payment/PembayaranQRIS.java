package com.tiketin.payment;

import com.tiketin.enums.StatusPembayaran;
import com.tiketin.model.DataPembayaran;

public class PembayaranQRIS implements Pembayaran{
    
    @Override 
    public String getMetode(){
        return "QRIS";
    }

    @Override 
    public DataPembayaran proses(int idPemesanan, double jumlahBayar){
        DataPembayaran data = new DataPembayaran();
        data.setIdPemesanan(idPemesanan);
        data.setMetodePembayaran(getMetode());
        data.setStatusPembayaran(StatusPembayaran.BERHASIL);
        data.setJumlahBayar(jumlahBayar);
        data.setJumlahUangDiterima(null);
        data.setKembalian(null);
        return data;
    }
}
