package org.example.gui.controller;

import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class LandingAcademicController extends LandingController {
  public void navigateToReviewStudentListPage() {
    ReviewStudentListController.navigateToStudentListPage(getScene());
  }

  public void handleOpenRegisterStudent() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Estudiante", "RegisterStudentModal")
    );
  }

  public void navigateToReviewEvaluationListPage() {
    navigateFromThisPageTo("Listado de Evaluaciones", "ReviewEvaluationListPage");
  }
}

