package com.tiketin.controller.admin;

import com.tiketin.model.Admin;
import com.tiketin.model.RingkasanDashboard;
import com.tiketin.model.TitikGrafik;
import com.tiketin.service.LaporanService;
import com.tiketin.util.Rupiah;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

public class DashboardController implements HalamanAdmin {
    @FXML
    private Label sapaan;
    @FXML
    private Label jumlahFilm;
    @FXML
    private Label jumlahJadwal;
    @FXML
    private Label jumlahTransaksi;
    @FXML
    private Label pendapatan;
    @FXML
    private Label catatanKosong;
    @FXML
    private AreaChart<String, Number> grafikPendapatan;
    @FXML
    private BarChart<String, Number> grafik;

    private final LaporanService laporanService = new LaporanService();

    @Override
    public void siap(Admin admin) {
        sapaan.setText("Halo, " + admin.getNama());
        RingkasanDashboard ringkas = laporanService.dashboard();
        jumlahFilm.setText(String.valueOf(ringkas.getJumlahFilm()));
        jumlahJadwal.setText(String.valueOf(ringkas.getJumlahJadwalAktif()));
        jumlahTransaksi.setText(String.valueOf(ringkas.getJumlahTransaksi()));
        pendapatan.setText(Rupiah.dari(ringkas.getPendapatan()));
        XYChart.Series<String, Number> seriPendapatan = new XYChart.Series<>();
        seriPendapatan.setName("Pendapatan");
        XYChart.Series<String, Number> seriJumlah = new XYChart.Series<>();
        seriJumlah.setName("Transaksi");
        boolean adaPesanan = false;
        for (TitikGrafik titik : ringkas.getGrafik()) {
            seriPendapatan.getData().add(new XYChart.Data<>(titik.getLabel(), titik.getNilai()));
            seriJumlah.getData().add(new XYChart.Data<>(titik.getLabel(), titik.getJumlah()));
            if (titik.getNilai() > 0 || titik.getJumlah() > 0) {
                adaPesanan = true;
            }
        }
        grafikPendapatan.getData().setAll(seriPendapatan);
        grafik.getData().setAll(seriJumlah);
        aturSumbuJumlah(seriJumlah);
        catatanKosong.setVisible(!adaPesanan);
        catatanKosong.setManaged(!adaPesanan);
    }

    private void aturSumbuJumlah(XYChart.Series<String, Number> seri) {
        int max = 0;
        for (XYChart.Data<String, Number> data : seri.getData()) {
            max = Math.max(max, data.getYValue().intValue());
        }
        int puncak = Math.max(1, max);
        int langkah = puncak <= 5 ? 1 : (int) Math.ceil(puncak / 4.0);
        NumberAxis sumbu = (NumberAxis) grafik.getYAxis();
        sumbu.setAutoRanging(false);
        sumbu.setLowerBound(0);
        sumbu.setUpperBound(puncak + langkah);
        sumbu.setTickUnit(langkah);
        sumbu.setMinorTickCount(0);
        sumbu.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number nilai) {
                if (nilai == null) {
                    return "";
                }
                double angka = nilai.doubleValue();
                if (Math.abs(angka - Math.rint(angka)) > 0.001) {
                    return "";
                }
                return Integer.toString((int) Math.rint(angka));
            }

            @Override
            public Number fromString(String teks) {
                return 0;
            }
        });
    }
}
