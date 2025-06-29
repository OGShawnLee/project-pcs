package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.business.auth.AuthClient;
import org.example.common.ExceptionHandler;
import org.example.gui.AlertFacade;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public abstract class Controller {
  private static final Logger CONTROLLER_LOGGER = LogManager.getLogger(Controller.class);
  @FXML
  protected Node container;

  protected Stage getScene() {
    return (Stage) container.getScene().getWindow();
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
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleGUILoadIOException(CONTROLLER_LOGGER, e).getMessage()
      );
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
