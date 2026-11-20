package com.tiketin;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Font Inter dari Alia (untuk halaman kiosk)
        Font.loadFont(App.class.getResourceAsStream("/com/tiketin/fonts/Inter-Regular.ttf"), 14);
        Font.loadFont(App.class.getResourceAsStream("/com/tiketin/fonts/Inter-SemiBold.ttf"), 14);
        Font.loadFont(App.class.getResourceAsStream("/com/tiketin/fonts/Inter-Bold.ttf"), 14);

        // Aplikasi tetap dibuka dari halaman login
        scene = new Scene(loadFXML("fxml/login"));

        stage.setTitle("CineBook");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        if (fxml.contains("customer/")) {
            pasangCssKiosk(root);
        }
        scene.setRoot(root);
    }

    // Dipakai halaman kiosk (customer)
    public static void setRoot(Parent root) {
        pasangCssKiosk(root);
        scene.setRoot(root);
    }

    // CSS kiosk hanya dipasang di halaman kiosk, supaya tampilan admin & kasir tidak berubah
    private static void pasangCssKiosk(Parent root) {
        URL css = App.class.getResource("/com/tiketin/css/kiosk.css");
        if (css != null && !root.getStylesheets().contains(css.toExternalForm())) {
            root.getStylesheets().add(css.toExternalForm());
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
