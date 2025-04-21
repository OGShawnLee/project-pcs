package org.example.gui;

import javafx.scene.control.Alert;

public class AlertDialog {
  public static void showError(String message) {
    Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error de Validación");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showSuccess(String message) {
    Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    alert.setTitle("Éxito");
    alert.setHeaderText("Registro Exitoso");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
