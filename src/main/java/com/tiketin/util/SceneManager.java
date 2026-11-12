package com.tiketin.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    private SceneManager(){
    }
        public static void setStage(Stage stageUtama){
            stage = stageUtama;
        }
        
        public static void pindah(String namaFxml, String judul){
            try{
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/com/tiketin/fxml/" + namaFxml + ".fxml"));
                Parent root = loader.load();

                if (stage.getScene() == null){
                    stage.setScene(new Scene(root));
                } else {
                    stage.getScene().setRoot(root);
                }
                stage.setTitle(judul);
                stage.show();
            } catch (IOException e){
                throw new RuntimeException("Gagal membuka halaman: " + namaFxml, e);
            }
        
    }
}
