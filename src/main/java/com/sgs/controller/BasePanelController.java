package com.sgs.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BasePanelController {

    @FXML protected Label lblTitle;
    @FXML protected Label lblUser;
    @FXML protected AnchorPane mainContent;

    public void setUserInfo(String role, String userName) {
        lblTitle.setText("SGS - Panel " + role.toUpperCase());
        lblUser.setText("Usuario: " + userName);
    }

    public void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane content = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(content);

            // Hacer que el contenido se ajuste al anchor pane
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Pane root = loader.load();
            mainContent.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
