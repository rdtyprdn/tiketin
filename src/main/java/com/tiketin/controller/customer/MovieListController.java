package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MovieListController {

    @FXML
    private void handleZootopia() {
        bukaDetailFilm(
            "Zootopia 2",
            "Animation",
            "108 min",
            "7.3",
            "Kemitraan polisi Judy Hopps dan Nick Wilde diuji saat mereka ditugaskan melacak Gary De'Snake, seekor ular piton misterius yang memicu kekacauan besar di Zootopia.",
            "Jared Bush & Byron Howard",
            "2025",
            "SU",
            "/com/tiketin/images/Zootopia.jpg"
        );
    }

    @FXML
    private void handleRanggaCinta() {
        bukaDetailFilm(
            "Rangga & Cinta",
            "Romance",
            "112 min",
            "6.6",
            "Cinta, siswi SMA populer dan berprestasi, harus menerima kekalahan dalam lomba puisi dari Rangga, siswa yang kurang dikenal di sekolahnya. Pertemuan tersebut membuat keduanya saling tidak menyukai, tetapi seiring waktu, rasa benci itu perlahan berubah menjadi cinta.",
            "Riri Riza",
            "2025",
            "R13",
            "/com/tiketin/images/Rangga dan Cinta.jpg"
        );
    }

    @FXML
    private void handleGhost() {
        bukaDetailFilm(
            "Ghost In The Cell",
            "Horor",
            "155 min",
            "6.5",
            "Di sebuah penjara, para narapidana diteror oleh sosok hantu misterius yang memburu orang-orang dengan aura negatif. Untuk bertahan hidup, mereka harus mengesampingkan permusuhan dan bekerja sama menghadapi teror tersebut.",
            "Joko Anwar",
            "2026",
            "17+",
            "/com/tiketin/images/ghost.jpg"
        );
    }

    @FXML
    private void handleWicked() {
        bukaDetailFilm(
            "Wicked",
            "Fantasy",
            "160 min",
            "7.3",
            "Elphaba dan Glinda, dua gadis dengan kepribadian berbeda, bertemu di Universitas Shiz dan menjalin persahabatan. Namun, sebuah peristiwa membuat mereka menghadapi pilihan yang mengubah kehidupan dan takdir mereka di Negeri Oz.",
            "Jon M. Chu.",
            "2024",
            "8+",
            "/com/tiketin/images/Wicked.jpg"
        );
    }

    @FXML
    private void handleMissionImpossible() {
        bukaDetailFilm(
            "Mission: Impossible Fallout",
            "Action",
            "155 min",
            "7.7",
            "Ethan Hunt dan tim IMF berusaha menghentikan kelompok teroris yang berencana melancarkan serangan nuklir. Mereka harus berpacu dengan waktu demi menyelamatkan dunia dari kehancuran.",
            "Christopher McQuarrie",
            "2018",
            "PG-13",
            "/com/tiketin/images/Action.jpg"
        );
    }

    private void bukaDetailFilm(
            String judul,
            String genre,
            String durasi,
            String rating,
            String sinopsis,
            String sutradara,
            String tahun,
            String usia,
            String poster) {

        try {
            FXMLLoader loader = new FXMLLoader(
                App.class.getResource(
                    "/com/tiketin/fxml/customer/movie-detail.fxml"
                )
            );

            Parent root = loader.load();

            MovieDetailController controller = loader.getController();

            controller.setDataFilm(
                judul, genre, durasi, rating,
                sinopsis, sutradara, tahun, usia
            );

            controller.setPoster(poster);

            App.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleKembali() {  
    try {
        App.setRoot("fxml/customer/kiosk-home");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}