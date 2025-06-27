module com.sgs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires net.sf.jasperreports.core;
    requires java.desktop;

    exports com.sgs;
    exports com.sgs.controller;
    exports com.sgs.util.report;

    opens com.sgs to javafx.fxml;
    opens com.sgs.controller to javafx.fxml;

    opens com.sgs.model to net.sf.jasperreports.core, java.base;

    exports com.sgs.model;
}