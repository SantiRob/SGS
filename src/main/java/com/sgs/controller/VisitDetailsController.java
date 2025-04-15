package com.sgs.controller;

import com.sgs.model.Visit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class VisitDetailsController {
    private Stage stage;
    @FXML private Label lblStation;
    @FXML private Label lblUser;
    @FXML private Label lblType;
    @FXML private Label lblDate;
    @FXML private Label lblResult;
    @FXML private TextArea txtObservations;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void onClose() {
        if (stage != null) {
            stage.close();
        } else {
            System.out.println("Stage no fue inicializado correctamente.");
        }
    }

    public void loadData(Visit visit, String stationName, String userName, String typeName) {
        lblStation.setText(stationName);
        lblUser.setText(userName);
        lblType.setText(typeName);
        lblDate.setText(visit.getDate().toString());
        lblResult.setText(visit.getResult());
        txtObservations.setText(visit.getObservations());
    }
}
