package org.example.gui.controller;

import javafx.stage.Stage;

public class LandingPageController extends Controller {
  public void navigateToReviewAcademicListPage() {
      ReviewAcademicListController.navigateToAcademicListPage(getScene());
  }

  public void navigateToReviewProjectListPage() {
      ReviewProjectListController.navigateToProjectListPage(getScene());
  }

  public void navigateToReviewOrganizationListPage() {
      ReviewOrganizationListController.navigateToOrganizationListPage(getScene());
  }

  public static void navigateToLandingPage(Stage currentStage) {
    navigateTo(currentStage,"Página de Inicio","LandingPage");
  }
}
