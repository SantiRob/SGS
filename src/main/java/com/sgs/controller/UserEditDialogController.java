package com.sgs.controller;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UserEditDialogController {

    @FXML private TextField tfName;
    @FXML private TextField tfEmail;
    @FXML private ChoiceBox<String> cbRole;

    private User user;
    private final UserRepository userRepository = new UserRepository();

    @FXML
    public void initialize() {
        cbRole.getItems().addAll("admin", "technician");
    }

    public void setUser(User user) {
        this.user = user;
        tfName.setText(user.getName());
        tfEmail.setText(user.getEmail());
        cbRole.setValue(user.getRole());
    }

    @FXML
    public void onSave() {
        String newName = tfName.getText();
        String newEmail = tfEmail.getText();
        String newRole = cbRole.getValue();

        if (newName.isBlank() || newEmail.isBlank() || newRole == null) {
            showAlert("Por favor completa todos los campos.");
            return;
        }

        user.setName(newName);
        user.setEmail(newEmail);
        user.setRole(newRole);

        userRepository.updateUser(user);
        close();
    }

    @FXML
    public void onCancel() {
        close();
    }

    private void close() {
        ((Stage) tfName.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
