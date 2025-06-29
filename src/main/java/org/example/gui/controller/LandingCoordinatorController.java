package org.example.gui.controller;

import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class LandingCoordinatorController extends LandingController {
  public void navigateToReviewAcademicListPage() {
    ReviewAcademicListController.navigateToAcademicListPage(getScene());
  }

  public void handleOpenRegisterAcademic() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Académico", "RegisterAcademicModal")
    );
  }

  public void navigateToReviewProjectListPage() {
    ReviewProjectListController.navigateToProjectListPage(getScene());
  }

  public void navigateToReviewRepresentativeListPage() {
    ReviewRepresentativeListController.navigateToRepresentativeListPage(getScene());
  }

  public void handleOpenRegisterRepresentative() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Representante", "RegisterRepresentativeModal")
    );
  }

  public void handleOpenRegisterProject() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Proyecto", "RegisterProjectModal")
    );
  }

  public void navigateToReviewCourseListPage() {
    ReviewCourseListController.navigateToCourseListPage(getScene());
  }

  public void handleOpenRegisterCourse() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Curso", "RegisterCourseModal")
    );
  }

  public void navigateToReviewOrganizationListPage() {
    ReviewOrganizationListController.navigateToOrganizationListPage(getScene());
  }

  public void handleOpenRegisterOrganization() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Organización", "RegisterOrganizationModal")
    );
  }

  public void handleOpenReviewStats() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Estadísticas", "ReviewStatsModal")
    );
  }

  public void handleOpenManageConfiguration() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Configuración", "ManageConfigurationModal")
    );
  }
}