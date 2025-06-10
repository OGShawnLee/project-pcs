package org.example.gui.controller;


public class LandingAcademicController extends LandingController {
  public void navigateToReviewStudentListPage() {
    navigateFromThisPageTo("Listado de Estudiantes", "ReviewStudentListPage");
  }

  public void handleOpenRegisterStudent() {
    navigateFromThisPageTo("Registrar Estudiante", "RegisterStudentPage");
  }

  public void navigateToReviewEvaluationListPage() {
    navigateFromThisPageTo("Listado de Evaluaciones", "ReviewEvaluationListPage");
  }
}

