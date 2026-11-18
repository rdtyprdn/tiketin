package com.tiketin.component;

import com.tiketin.dao.DetailPemesananDAO;
import com.tiketin.dao.KursiDAO;
import com.tiketin.model.Kursi;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class DenahKursi extends GridPane {

    private final List<Integer> kursiDipilih = new ArrayList<>();
    private final KursiDAO kursiDAO = new KursiDAO();
    private final DetailPemesananDAO detailDAO = new DetailPemesananDAO();

    public DenahKursi() {
        setHgap(6);
        setVgap(6);
        setPadding(new Insets(10));
    }

    // ini dipanggil setelah user milih jadwal
    public void muat(int idStudio, int idJadwal) throws SQLException {
        getChildren().clear();
        kursiDipilih.clear();

        List<Kursi> daftarKursi = kursiDAO.getByStudio(idStudio);
        Set<Integer> terisi = detailDAO.getKursiTerisi(idJadwal);

        for (Kursi k : daftarKursi) {
            String nomor = k.getNomorKursi();             // contoh "A10"
            int baris = nomor.charAt(0) - 'A';            // A=0, B=1, dst ...
            int kolom = Integer.parseInt(nomor.substring(1)) - 1; // 10 -> 9

            Button tombol = new Button(nomor);
            tombol.setPrefSize(48, 36);
            tombol.getStyleClass().add("kursi");

            if (terisi.contains(k.getIdKursi())) {
                tombol.setDisable(true);
                tombol.getStyleClass().add("kursi-terisi");
            } else {
                tombol.setOnAction(e -> pilih(k.getIdKursi(), tombol));
            }

            add(tombol, kolom, baris);
        }
    }

    private void pilih(int idKursi, Button tombol) {
        if (kursiDipilih.contains(idKursi)) {
            kursiDipilih.remove(Integer.valueOf(idKursi));
            tombol.getStyleClass().remove("kursi-dipilih");
        } else {
            kursiDipilih.add(idKursi);
            tombol.getStyleClass().add("kursi-dipilih");
        }
    }

    public List<Integer> getKursiDipilih() {
        return new ArrayList<>(kursiDipilih);
    }
}