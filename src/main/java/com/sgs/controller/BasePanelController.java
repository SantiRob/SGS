package com.sgs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BasePanelController {

    @FXML private Label lblTitle;
    @FXML private Label lblUser;
    @FXML private AnchorPane mainContent;
    @FXML private VBox sideMenu;

    @FXML private Button btnUsers;
    @FXML private Button btnStations;
    @FXML private Button btnTypes;

    private String currentRole;

    // Este método se llama desde LoginController al cargar panel-layout.fxml
    public void setUserInfo(String role, String userName) {
        currentRole = role.toLowerCase();
        lblTitle.setText("SGS - Panel " + role.toUpperCase());
        lblUser.setText("Usuario: " + userName);

        configureRoleAccess();
    }

    // Oculta botones según el rol
    private void configureRoleAccess() {
        if ("technician".equals(currentRole)) {
            btnUsers.setVisible(false);
            btnStations.setVisible(false);
            btnTypes.setVisible(false);
        }
    }

    // Botón de cerrar sesión
    @FXML
    public void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cargar vista dinámica dentro de mainContent
    public void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane content = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(content);

            // Hacer que se ajuste al tamaño del AnchorPane
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Acción para el botón "Usuarios"
    @FXML
    public void loadUsersView() {
        loadContent("/view/users-view.fxml");
    }
    // Acción para el botón "Estaciones"
    @FXML
    public void loadStationsView() {
        loadContent("/view/stations-view.fxml");
    }
    // Acción para el botón "Tipos"
    @FXML
    public void loadMaintenanceTypesView() {
        loadContent("/view/maintenance-types-view.fxml");
    }

}
