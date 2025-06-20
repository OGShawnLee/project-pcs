package org.example.gui.controller;


import org.example.gui.Modal;

public class LandingAcademicController extends LandingController {

  public void navigateToReviewStudentListPage() {
    ReviewStudentListController.navigateToStudentListPage(getScene());
  }

  public void handleOpenRegisterStudent() {
    Modal.display("Registrar Estudiante", "RegisterStudentModal");
  }

  public void navigateToReviewEvaluationListPage() {
    navigateFromThisPageTo("Listado de Evaluaciones", "ReviewEvaluationListPage");
  }
}

