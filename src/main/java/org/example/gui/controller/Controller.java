package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.example.business.auth.AuthClient;
import org.example.gui.Modal;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public abstract class Controller {
  @FXML
  protected AnchorPane container;

  protected Stage getScene() {
    return (Stage) container.getScene().getWindow();
  }

  protected static void loadRecordState(ComboBox<String> fieldState) {
    fieldState.getItems().setAll("Activo", "Inactivo");
    fieldState.setValue("Activo");
  }

  public void navigateToLandingPage() {
    switch (AuthClient.getInstance().getCurrentUser().role()) {
      case ACADEMIC -> navigateFromThisPageTo("Landing Page", "LandingAcademicPage");
      case ACADEMIC_EVALUATOR -> navigateFromThisPageTo("Landing Page", "LandingAcademicEvaluatorPage");
      case EVALUATOR -> navigateFromThisPageTo("Landing Page", "LandingEvaluatorPage");
      case COORDINATOR -> navigateFromThisPageTo("Landing Page", "LandingCoordinatorPage");
      case STUDENT -> navigateFromThisPageTo("Landing Page", "LandingStudentPage");
    }
  }

  protected void navigateFromThisPageTo(String pageName, String resourceFileName) {
    navigateTo(getScene(), pageName, resourceFileName);
  }

  protected static void navigateTo(Stage currentStage, String pageName, String resourceFileName) {
    try {
      Parent newView = FXMLLoader.load(
        Objects.requireNonNull(
          Controller.class.getResource("/org/example/" + resourceFileName + ".fxml")
        )
      );
      Scene newScene = new Scene(newView);

      currentStage.setScene(newScene);
      currentStage.show();
    } catch (IOException e) {
      e.printStackTrace();
      Modal.displayError("No ha sido posible navegar a: " + pageName + " debido a un error de sistema.");
    }
  }

  protected static <T> void navigateToManagePage(
    Stage currentStage,
    String pageName,
    String resourceFileName,
    T dataObject
  ) {
    try {
      FXMLLoader loader = new FXMLLoader(
        Objects.requireNonNull(Controller.class.getResource("/org/example/" + resourceFileName + ".fxml"))
      );
      Parent newView = loader.load();
      Scene newScene = new Scene(newView);

      ManageController<T> controller = loader.getController();
      controller.initialize(dataObject);

      currentStage.setScene(newScene);
      currentStage.show();
    } catch (IOException e) {
      Modal.displayError("No ha sido posible navegar a: " + pageName + " debido a un error de sistema.");
    }
  }

  protected <T> void setRowDoubleClickHandler(TableView<T> tableView, Function<T, Void> handler) {
    tableView.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        T selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
          handler.apply(selectedItem);
        }
      }
    });
  }
}
