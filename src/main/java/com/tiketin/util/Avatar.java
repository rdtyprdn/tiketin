package com.tiketin.util;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class Avatar {
    private Avatar() {
    }

    public static String inisial(String nama) {
        if (nama == null || nama.isBlank()) {
            return "?";
        }
        String[] kata = nama.trim().split("\\s+");
        String hasil = kata[0].substring(0, 1);
        if (kata.length > 1 && !kata[1].isBlank()) {
            hasil += kata[1].substring(0, 1);
        }
        return hasil.toUpperCase(Locale.of("id", "ID"));
    }

    public static void pasang(ImageView foto, Label inisial, String nama, String path, double ukuran) {
        inisial.setText(inisial(nama));
        Image gambar = baca(path, ukuran);
        if (gambar == null) {
            foto.setImage(null);
            foto.setVisible(false);
            foto.setManaged(false);
            inisial.setVisible(true);
            inisial.setManaged(true);
            return;
        }
        foto.setImage(gambar);
        foto.setClip(new Circle(ukuran / 2, ukuran / 2, ukuran / 2));
        foto.setVisible(true);
        foto.setManaged(true);
        inisial.setVisible(false);
        inisial.setManaged(false);
    }

    private static Image baca(String path, double ukuran) {
        if (path == null || path.isBlank() || !Files.isRegularFile(Path.of(path))) {
            return null;
        }
        Image gambar = new Image(Path.of(path).toUri().toString(), ukuran, ukuran, false, true);
        if (gambar.isError()) {
            return null;
        }
        return gambar;
    }
}
