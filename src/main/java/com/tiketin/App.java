package com.tiketin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    // private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/tiketin/fxml/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("CineBook");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();
    }

    // static void setRoot(String fxml) throws IOException {
    //     scene.setRoot(loadFXML(fxml));
    // }

    // private static Parent loadFXML(String fxml) throws IOException {
    //     FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    //     return fxmlLoader.load();
    // }

    public static void main(String[] args) {
        launch();
    }

}