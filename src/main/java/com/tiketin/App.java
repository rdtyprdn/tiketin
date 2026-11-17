package com.tiketin;

import com.tiketin.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.pindah("admin/Login", "Tiketin - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}