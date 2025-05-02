package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.business.controller.ReviewAcademicListController;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.util.Objects;

public class LandingPageController {
  @FXML
  private AnchorPane container;

  public void navigateToReviewAcademicListPage() {
    try {
      ReviewAcademicListController.navigateToAcademicListPage(
        (Stage) container.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de academicos."
      );
    }
  }

  public void navigateToReviewProjectListPage() {
    try {
      ReviewProjectListController.navigateToProjectListPage(
        (Stage) container.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de proyectos."
      );
    }
  }

  public static void navigateToLandingPage(Stage currentStage) throws IOException {
    Parent newView = FXMLLoader.load(
      Objects.requireNonNull(LandingPageController.class.getResource("/org/example/LandingPage.fxml"))
    );
    Scene newScene = new Scene(newView);

    currentStage.setScene(newScene);
    currentStage.show();
  }
}
