package com.sgs.controller;

import com.sgs.service.UserService;
import com.sgs.util.TextFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField tfName;
    @FXML private TextField tfSapUser;
    @FXML private TextField tfEmail;
    @FXML private PasswordField pfPassword;
    @FXML private PasswordField pfConfirmPassword;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private Label registerMessageLabel;

    private final UserService userService = new UserService();
    private final String[] roles = {"technician"};

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll(roles);

        // Validación para SAP User (solo números, máximo 12 caracteres)
        TextFieldValidator.setNumericOnly(tfSapUser, 12);

        // Validación para email en tiempo real
        TextFieldValidator.setEmailValidation(tfEmail);

        // Validación para nombre (solo letras)
        TextFieldValidator.setAlphaOnly(tfName);
    }

    @FXML
    public void registerButtonAction(ActionEvent e) {
        String name = tfName.getText();
        String sapUser = tfSapUser.getText();
        String email = tfEmail.getText();
        String role = roleChoiceBox.getValue();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (name.isBlank() || sapUser.isBlank() || email.isBlank() || role == null || password.isBlank() || confirmPassword.isBlank()) {
            registerMessageLabel.setText("Por favor completa todos los campos.");
            return;
        }

        if (!sapUser.matches("\\d+")) {
            registerMessageLabel.setText("El Usuario SAP debe contener solo números.");
            return;
        }

        if (sapUser.length() < 3 || sapUser.length() > 12) {
            registerMessageLabel.setText("El Usuario SAP debe tener entre 3 y 12 dígitos.");
            return;
        }

        if (!TextFieldValidator.isValidEmail(email)) {
            registerMessageLabel.setText("Por favor ingresa un email válido.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerMessageLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        if (userService.isSapUserRegistered(sapUser)) {
            registerMessageLabel.setText("Ese Usuario SAP ya está registrado.");
            return;
        }

        boolean success = userService.registerUser(name, sapUser, email, role, password);
        if (success) {
            registerMessageLabel.setText("Usuario registrado exitosamente.");
            clearForm();
        } else {
            registerMessageLabel.setText("Error al registrar usuario.");
        }
    }

    private void clearForm() {
        tfName.clear();
        tfSapUser.clear();
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