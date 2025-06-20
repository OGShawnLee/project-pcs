package org.example.gui.controller;

import org.example.business.dao.PracticeDAO;
import org.example.business.dto.PracticeDTO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class LandingEvaluatorController extends LandingController {
  public void navigateToReviewEvaluationListPage() {
    Modal.display("Registrar Solicitud de Proyecto", "RegisterProjectRequestPAge");
  }

  public void handleOpenRegisterEvaluation() {
    try {
      List<PracticeDTO> practices = new PracticeDAO().getAll();

      if (practices.isEmpty()) {
        Modal.displayError("No existen prácticas. Registre una antes de evaluar.");
        return;
      }

      Modal.display("Registrar Evaluacion", "RegisterEvaluationModal");
    } catch (SQLException e) {
      Modal.displayError("No fue posible recuperar las practicas profesionales");
    }
  }
}
