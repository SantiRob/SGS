package com.sgs.controller;

import com.sgs.model.Station;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StationDetailsController {
    private Stage stage;

    @FXML private Label lblStationID;
    @FXML private Label lblStationName;
    @FXML private Label lblStationAddress;
    @FXML private Label lblGasType;
    @FXML private Label lblStationStatus;
    @FXML private Label lblMalla;

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

    public void loadData(Station station) {
        lblStationID.setText(String.valueOf(station.getIdStation()));
        lblStationName.setText(station.getStation());
        lblStationAddress.setText(station.getAddress());
        lblGasType.setText(station.getType());
        lblStationStatus.setText(station.getStatus());
        lblMalla.setText(station.getMalla());
    }
}
