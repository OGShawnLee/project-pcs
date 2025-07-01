package org.example.gui.controller;

import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class LandingEvaluatorController extends LandingController {
  public void navigateToReviewEvaluationListPage() {
    ReviewEvaluationListController.navigateToEvaluationListPage(getScene());
  }

  public void handleOpenRegisterEvaluation() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Evaluación", "RegisterEvaluationModal")
    );
  }
}