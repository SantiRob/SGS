package com.sgs.controller;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.sgs.util.report.UserReportGenerator;

import java.io.File;

public class UsersController {

    @FXML
    private TableView<User> tableUsers;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TextField searchBar;

    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdUser()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

        loadUsers();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        FilteredList<User> filtered = new FilteredList<>(userList, u -> true);

        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            filtered.setPredicate(user -> {
                if (newVal == null || newVal.isBlank()) return true;
                return user.getName().toLowerCase().contains(lower) ||
                        user.getEmail().toLowerCase().contains(lower) ||
                        user.getRole().toLowerCase().contains(lower);
            });
        });

        SortedList<User> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableUsers.comparatorProperty());
        tableUsers.setItems(sorted);
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

    @FXML
    public void onShowDetails() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aviso", "Selecciona un usuario para ver los detalles.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user-details-view.fxml"));
            Parent root = loader.load();

            UserDetailsController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Detalles de Usuario");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            controller.setStage(stage);
            controller.loadData(selected);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo mostrar la vista de detalles.");
        }
    }

    @FXML
    public void onGenerateReport(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog("users-report");
        inputDialog.setTitle("Guardar Reporte de Usuarios");
        inputDialog.setHeaderText("Ingresa el nombre del archivo PDF:");
        inputDialog.setContentText("Nombre:");

        String fileName = inputDialog.showAndWait().orElse("").trim();
        if (fileName.isBlank()) {
            showAlert("Operación cancelada", "No se ingresó un nombre de archivo.");
            return;
        }

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona carpeta destino");

        File directory = chooser.showDialog(((javafx.scene.Node) event.getSource()).getScene().getWindow());
        if (directory == null) {
            showAlert("Operación cancelada", "No se seleccionó carpeta.");
            return;
        }
        String outputPath = directory.getAbsolutePath() + "/" + fileName + ".pdf";
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            showAlert("Archivo existente",
                    "Ya existe un archivo con ese nombre:\n" + outputPath + "\nPor favor, usa otro nombre o elimina el archivo.");
            return;
        }
        new UserReportGenerator().export(outputPath, userRepo);
    }
}
