package org.example.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.gui.controller.ContextController;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Modal {
  public static void display(String title, String resourceFileName) {
    display(title, resourceFileName, null);
  }

  public static void display(String title, String resourceFileName, Runnable onClose) {
    try {
      Parent root = FXMLLoader.load(
        Objects.requireNonNull(Modal.class.getResource("/org/example/" + resourceFileName + ".fxml"))
      );
      Scene newScene = new Scene(root);
      Stage modalStage = new Stage();

      if (onClose != null) {
        modalStage.setOnHidden(event -> onClose.run());
      }

      modalStage.setTitle(title);
      modalStage.setScene(newScene);
      modalStage.setResizable(false);
      modalStage.initModality(Modality.APPLICATION_MODAL);
      modalStage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
      Modal.displayError("No ha sido posible cargar modal.");
    }
  }

  public static <T> void displayContextModal(String title, String resourceFileName, T dataObject) {
    displayContextModal(title, resourceFileName, null, dataObject);
  }

  public static <T> void displayContextModal(String title, String resourceFileName, Runnable onClose, T dataObject) {
    try {
      FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(Modal.class.getResource("/org/example/" + resourceFileName + ".fxml"))
      );

      Parent root = loader.load();
      Scene newScene = new Scene(root);
      Stage modalStage = new Stage();

      if (onClose != null) {
        modalStage.setOnHidden(event -> onClose.run());
      }

      ContextController<T> controller = loader.getController();
      controller.initialize(dataObject);

      modalStage.setTitle(title);
      modalStage.setScene(newScene);
      modalStage.setResizable(false);
      modalStage.initModality(Modality.APPLICATION_MODAL);
      modalStage.showAndWait();
    } catch (IOException e) {
      displayError("No ha sido posible cargar el modal de gestión.");
    }
  }

  public static void displayError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error de Validación");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void displayInformation(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Información");
    alert.setHeaderText("Información del Sistema");
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
}
