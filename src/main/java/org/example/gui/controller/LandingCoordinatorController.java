package org.example.gui.controller;

import org.example.gui.Modal;

public class LandingCoordinatorController extends LandingController {
  public void navigateToReviewAcademicListPage() {
    ReviewAcademicListController.navigateToAcademicListPage(getScene());
  }

  public void handleOpenRegisterAcademic() {
    Modal.display("Registrar Académico", "RegisterAcademicModal");
  }

  public void navigateToReviewProjectListPage() {
    ReviewProjectListController.navigateToProjectListPage(getScene());
  }

  public void handleOpenRegisterProject() {
    Modal.display("Registrar Proyecto", "RegisterProjectModal");
  }

  public void navigateToReviewOrganizationListPage() {
    ReviewOrganizationListController.navigateToOrganizationListPage(getScene());
  }

  public void handleOpenRegisterOrganization() {
    Modal.display("Registrar Organización", "RegisterOrganizationModal");
  }

  public void handleOpenReviewStats() {
    Modal.display("Estadísticas", "ReviewStatsModal");
  }

  public void handleOpenManageConfiguration() {
    Modal.display("Configuración", "ManageConfigurationModal");
  }
}
