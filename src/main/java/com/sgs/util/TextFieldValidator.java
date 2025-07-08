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

    /**
     * Valida formato de email en tiempo real
     * Previene múltiples puntos consecutivos después del dominio
     * @param textField El campo a validar
     */
    public static void setEmailValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            if (!newValue.matches("[a-zA-Z0-9@._-]*")) {
                textField.setText(oldValue);
                return;
            }

            long atCount = newValue.chars().filter(ch -> ch == '@').count();
            if (atCount > 1) {
                textField.setText(oldValue);
                return;
            }

            if (newValue.contains("@")) {
                String[] parts = newValue.split("@", 2);
                if (parts.length == 2) {
                    String domain = parts[1];

                    if (domain.contains("..")) {
                        textField.setText(oldValue);
                        return;
                    }

                    if (domain.matches(".*\\.[a-zA-Z]{2,4}\\..+")) {
                        textField.setText(oldValue);
                        return;
                    }

                    if (domain.startsWith(".")) {
                        textField.setText(oldValue);
                        return;
                    }
                }
            }

            if (newValue.startsWith("@")) {
                textField.setText(oldValue);
                return;
            }
        });
    }

    /**
     * Valida si un email tiene formato válido (para validación final)
     * @param email El email a validar
     * @return true si es válido, false si no
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,8}$";

        if (!email.matches(emailPattern)) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.contains("..") || domain.contains("..")) {
            return false;
        }

        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }

        if (!domain.contains(".")) {
            return false;
        }

        if (domain.endsWith(".")) {
            return false;
        }

        String[] domainParts = domain.split("\\.");
        if (domainParts.length > 2) {
            String lastPart = domainParts[domainParts.length - 1];
            String secondLastPart = domainParts[domainParts.length - 2];

            if (secondLastPart.matches("[a-zA-Z]{2,4}") && lastPart.matches("[a-zA-Z]{2,8}")) {
                return false;
            }
        }

        return true;
    }
}