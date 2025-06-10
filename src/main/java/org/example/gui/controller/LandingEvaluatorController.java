package org.example.gui.controller;

public class LandingEvaluatorController extends LandingController {
  public void navigateToReviewEvaluationListPage() {
    navigateFromThisPageTo("Listado de Evaluaciones", "ReviewEvaluationListPage");
  }

  public void handleOpenRegisterEvaluation() {
    navigateFromThisPageTo("Registrar Evaluación", "RegisterEvaluationPage");
  }
}
