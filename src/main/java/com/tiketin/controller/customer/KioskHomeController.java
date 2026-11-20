
package com.tiketin.controller.customer;

import java.io.IOException;

import com.tiketin.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KioskHomeController {

    @FXML
    private Button mulaiPesanButton;

    @FXML
    private void handleMulaiPesan() {
        try {
            App.setRoot("fxml/customer/movie-list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
