package com.tiketin.util;

import com.tiketin.model.KasirRiwayatItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KasirRiwayatData {

    private static final ObservableList<KasirRiwayatItem> daftarTransaksi =
        FXCollections.observableArrayList();

    public static void tambahTransaksi(KasirRiwayatItem transaksi) {
        daftarTransaksi.add(0, transaksi);
    }

    public static ObservableList<KasirRiwayatItem> getDaftarTransaksi() {
        return daftarTransaksi;
    }
}