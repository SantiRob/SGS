package com.sgs.controller;

import com.sgs.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserDetailsController {
    private Stage stage;
    @FXML private Label lblUserID;
    @FXML private Label lblUserName;
    @FXML private Label lblUserEmail;
    @FXML private Label lblUserRol;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void onClose() {
        if (stage != null) {
            stage.close();
        } else {
            System.out.println("Stage no fue inicializado correctamente.");
        }
    }

    public void loadData(User user) {
        this.lblUserID.setText(String.valueOf(user.getIdUser()));
        this.lblUserName.setText(user.getName());
        this.lblUserEmail.setText(user.getEmail());
        this.lblUserRol.setText(user.getRole().toString());
    }
}
