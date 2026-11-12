package com.tiketin.payment;

import com.tiketin.model.DataPembayaran;;

public interface Pembayaran {
    String getMetode();

    DataPembayaran proses(int idPemesanan, double jumlahBayar);
}
