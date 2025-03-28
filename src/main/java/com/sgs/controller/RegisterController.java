package com.sgs.controller;

import com.sgs.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField tfName;
    @FXML private TextField tfEmail;
    @FXML private PasswordField pfPassword;
    @FXML private PasswordField pfConfirmPassword;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private Label registerMessageLabel;

    private final UserService userService = new UserService();
    private final String[] roles = {"admin", "technician"};

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll(roles);
    }

    @FXML
    public void registerButtonAction(ActionEvent e) {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String role = roleChoiceBox.getValue();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (name.isBlank() || email.isBlank() || role == null || password.isBlank() || confirmPassword.isBlank()) {
            registerMessageLabel.setText("Por favor completa todos los campos.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerMessageLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        if (userService.isEmailRegistered(email)) {
            registerMessageLabel.setText("Ese correo ya está registrado.");
            return;
        }

        boolean success = userService.registerUser(name, email, role, password);
        if (success) {
            registerMessageLabel.setText("Usuario registrado exitosamente.");
            clearForm();
        } else {
            registerMessageLabel.setText("Error al registrar usuario.");
        }
    }

    private void clearForm() {
        tfName.clear();
        tfEmail.clear();
        pfPassword.clear();
        pfConfirmPassword.clear();
        roleChoiceBox.setValue(null);
    }

    @FXML
    public void goToLogin(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Pane root = loader.load();
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            registerMessageLabel.setText("No se pudo cargar la vista de login.");
            ex.printStackTrace();
        }
    }
}
