package com.sgs.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TextFieldValidator {

    /**
     * Hace que un TextField solo acepte números
     * @param textField El campo a validar
     */
    public static void setNumericOnly(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Hace que un TextField solo acepte números con un límite de caracteres
     * @param textField El campo a validar
     * @param maxLength Número máximo de caracteres
     */
    public static void setNumericOnly(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Hace que un TextField solo acepte números usando TextFormatter (más robusto)
     * @param textField El campo a validar
     */
    public static void setNumericOnlyWithFormatter(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        textField.setTextFormatter(formatter);
    }

    /**
     * Hace que un TextField solo acepte números decimales
     * @param textField El campo a validar
     */
    public static void setDecimalOnly(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Hace que un TextField solo acepte letras
     * @param textField El campo a validar
     */
    public static void setAlphaOnly(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                textField.setText(oldValue);
            }
        });
    }
}