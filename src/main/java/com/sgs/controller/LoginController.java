package com.sgs.controller;

import com.sgs.model.User;
import com.sgs.service.UserService;
import com.sgs.util.TextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField tfSapUser;
    @FXML private PasswordField pfPassword;
    @FXML private Label loginMessageLabel;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
                TextFieldValidator.setNumericOnly(tfSapUser, 12);
    }

    @FXML
    public void loginButtonAction(ActionEvent e) {
        String sapUser  = tfSapUser.getText();
        String password = pfPassword.getText();

        if (sapUser.isBlank() || password.isBlank()) {
            loginMessageLabel.setText("Por favor completa todos los campos.");
            return;
        }

        User user = userService.login(sapUser, password);
        if (user != null) {
            loginMessageLabel.setText("Bienvenido " + user.getName() + " (" + user.getRole() + ")");
            loadPanelWithUser(user, e);
        } else {
            loginMessageLabel.setText("Correo o contraseña incorrecta.");
        }
    }

    private void loadPanelWithUser(User user, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/panel-layout.fxml"));
            Parent root = loader.load();

            BasePanelController controller = loader.getController();
            controller.setUserInfo(user.getRole(), user.getName());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SGS - Panel " + user.getRole());
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            loginMessageLabel.setText("No se pudo cargar el panel.");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            loginMessageLabel.setText("No se pudo cargar la pantalla de registro.");
        }
    }
}