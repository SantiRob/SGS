package com.sgs.controller;

import com.sgs.model.User;
import com.sgs.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField tfEmail;
    @FXML private PasswordField pfPassword;
    @FXML private Label loginMessageLabel;

    private final UserService userService = new UserService();

    @FXML
    public void loginButtonAction(ActionEvent e) {
        String email = tfEmail.getText();
        String password = pfPassword.getText();

        if (email.isBlank() || password.isBlank()) {
            loginMessageLabel.setText("Por favor completa todos los campos.");
            return;
        }

        User user = userService.login(email, password);
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

            // Obtener el controlador de la plantilla y pasarle los datos del usuario
            BasePanelController controller = loader.getController();
            controller.setUserInfo(user.getRole(), user.getName());

            // Cargar escena
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
