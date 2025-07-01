package org.example.gui.controller;

import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class LandingEvaluatorController extends LandingController {
  public void navigateToReviewEvaluationListPage() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Lista de Evaluaciones", "ReviewEvaluationListPage")
    );
  }

  public void handleOpenRegisterEvaluation() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Evaluación", "RegisterEvaluationModal")
    );
  }
}