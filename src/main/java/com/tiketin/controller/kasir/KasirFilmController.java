package com.tiketin.controller.kasir;

import java.io.IOException;

import com.tiketin.App;
import com.tiketin.model.Film;
import com.tiketin.util.KasirSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KasirFilmController {

    @FXML
    private void kembali() throws IOException {
        App.setRoot("fxml/kasir/KasirHome");
    }
    
    @FXML
private void pilihFilm(ActionEvent event) throws IOException {

    Button tombol = (Button) event.getSource();
    System.out.println("ID tombol: " + tombol.getId());
    Film film;

    switch (tombol.getId()) {

        case "btnZootopia":
            film = new Film(
                "Zootopia 2",
                "Animasi",
                108,
                "Petualangan Judy Hopps dan Nick Wilde dalam memecahkan misteri baru.",
                "Zootopia.jpg"
            );
            break;

        case "btnRanggaCinta":
            film = new Film(
                "Rangga & Cinta",
                "Romance",
                112,
                "Kisah perjalanan cinta Rangga dan Cinta.",
                "Rangga dan Cinta.jpg"
            );
            break;

        case "btnGhost":
            film = new Film(
                "Ghost In The Cell",
                "Horror",
                155,
                "Kisah misteri menyeramkan di dalam penjara.",
                "ghost.jpg"
            );
            break;

        case "btnWicked":
            film = new Film(
                "Wicked",
                "Fantasy",
                160,
                "Kisah persahabatan dan petualangan di dunia Oz.",
                "Wicked.jpg"
            );
            break;

        case "btnMissionImpossible":
            film = new Film(
                "Mission: Impossible Fallout",
                "Action",
                155,
                "Ethan Hunt menjalankan misi berbahaya untuk menyelamatkan dunia.",
                "Action.jpg"
            );
            break;

        default:
            return;
    }

    KasirSession.setFilmTerpilih(film);

    App.setRoot("fxml/kasir/KasirJadwal");
}
}

