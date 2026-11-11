package com.tiketin.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class Peringatan {
    private Peringatan() {
    }

    public static void gagal(String pesan) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("CineBook");
        alert.setHeaderText("Tidak bisa diproses");
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    public static boolean konfirmasi(String pesan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CineBook");
        alert.setHeaderText("Konfirmasi");
        alert.setContentText(pesan);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
