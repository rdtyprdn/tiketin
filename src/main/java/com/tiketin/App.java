
package com.tiketin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import javafx.scene.text.Font;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        var regular = Font.loadFont(App.class.getResourceAsStream(
            "/com/tiketin/fonts/Inter-Regular.ttf"), 14);
        var semiBold = Font.loadFont(App.class.getResourceAsStream(
            "/com/tiketin/fonts/Inter-SemiBold.ttf"), 14);
        var bold = Font.loadFont(App.class.getResourceAsStream(
            "/com/tiketin/fonts/Inter-Bold.ttf"), 14);

        System.out.println("Inter Regular: " + regular);
        System.out.println("Inter SemiBold: " + semiBold);
        System.out.println("Inter Bold: " + bold);

        scene = new Scene(loadFXML("fxml/customer/kiosk-home"), 1280, 720);

        URL css = App.class.getResource("/com/tiketin/css/global.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            throw new IllegalStateException("File global.css tidak ditemukan!");
        }

        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setRoot(Parent root) {
        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            App.class.getResource(fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
