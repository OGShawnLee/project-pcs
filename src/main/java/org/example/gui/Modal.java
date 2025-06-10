package org.example.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Modal {
  public static void display(String title, String resourceFileName) {
    try {
      Parent root = FXMLLoader.load(
        Objects.requireNonNull(Modal.class.getResource("/org/example/" + resourceFileName + ".fxml"))
      );
      Scene newScene = new Scene(root);
      Stage modalStage = new Stage();

      modalStage.setTitle(title);
      modalStage.setScene(newScene);
      modalStage.setResizable(false);
      modalStage.initModality(Modality.APPLICATION_MODAL);
      modalStage.showAndWait();
    } catch (IOException e) {
      Modal.displayError("No ha sido posible cargar modal.");
    }
  }

  public static void displayError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error de Validación");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void displaySuccess(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Éxito");
    alert.setHeaderText("Registro Exitoso");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static Optional<String> promptText(String title, String header, String content) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(header);
    dialog.setContentText(content);
    return dialog.showAndWait();
  }

  public static Optional<Boolean> promptConfirmation(String title, String header, String content) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    Optional<ButtonType> result = alert.showAndWait();
    return result.map(button -> button == ButtonType.OK);
  }
}
