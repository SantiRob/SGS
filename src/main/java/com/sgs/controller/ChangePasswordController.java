package com.sgs.controller;

import com.sgs.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordController {

    @FXML private PasswordField pfPassword;
    @FXML private PasswordField pfConfirm;

    private int userId;
    private final UserRepository userRepo = new UserRepository();

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void onSave() {
        String pass = pfPassword.getText();
        String confirm = pfConfirm.getText();

        if (pass.isBlank() || confirm.isBlank()) {
            showAlert("Campos vacíos", "Por favor completa ambos campos.");
            return;
        }

        if (!pass.equals(confirm)) {
            showAlert("Error", "Las contraseñas no coinciden.");
            return;
        }

        userRepo.updatePassword(userId, pass);
        close();
    }

    @FXML
    public void onCancel() {
        close();
    }

    private void close() {
        ((Stage) pfPassword.getScene().getWindow()).close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
