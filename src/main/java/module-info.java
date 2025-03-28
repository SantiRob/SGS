module com.sgs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.sgs to javafx.fxml;
    opens com.sgs.controller to javafx.fxml;
    exports com.sgs;
    exports com.sgs.controller;
}