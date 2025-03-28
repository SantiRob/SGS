package com.sgs.controller;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class UsersController {

    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;

    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdUser()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userRepo.findAll());
        tableUsers.setItems(userList);
    }

    @FXML
    public void onEditUser() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Selecciona un usuario para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user-edit-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            UserEditDialogController controller = loader.getController();
            controller.setUser(selected);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Editar Usuario");
            dialog.showAndWait();

            loadUsers(); // refresca tabla
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar la ventana de edición.");
        }
    }

    @FXML
    public void onDeleteUser() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean deleted = userRepo.deleteById(selected.getIdUser());
            if (deleted) {
                showAlert("Éxito", "Usuario eliminado.");
                loadUsers();
            } else {
                showAlert("Error", "No se pudo eliminar el usuario.");
            }
        } else {
            showAlert("Error", "Selecciona un usuario para eliminar.");
        }
    }

    @FXML
    public void onChangePassword() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Selecciona un usuario para cambiar contraseña.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/change-password-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.setUserId(selected.getIdUser());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Cambiar Contraseña");
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
