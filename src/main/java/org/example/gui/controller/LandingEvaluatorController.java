package org.example.gui.controller;

import org.example.business.dao.Configuration;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dao.PracticeDAO;
import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dto.PracticeDTO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class LandingEvaluatorController extends LandingController {
  public void navigateToReviewEvaluationListPage() {
    Modal.display("Lista de Evaluaciones", "ReviewEvaluationListPage");
  }

  public void handleOpenRegisterEvaluation() {
    try {
      List<PracticeDTO> practices = new PracticeDAO().getAll();

      if (practices.isEmpty()) {
        Modal.displayError("No existen prácticas. Registre una antes de evaluar.");
        return;
      }

      List<ConfigurationDTO> currentConfiguration = new ConfigurationDAO().getAll();

      for (ConfigurationDTO config : currentConfiguration) {
        String configName = String.valueOf(config.name());
        if (configName.equals(Configuration.EVALUATION_ENABLED_FIRST.name()) && config.isEnabled()) {
          Modal.display("Registrar Evaluacion-1er Parcial", "RegisterEvaluationModal");
        } else if (configName.equals(Configuration.EVALUATION_ENABLED_SECOND.name()) && config.isEnabled()) {
          Modal.display("Registrar Evaluacion-2do Parcial", "RegisterEvaluationModal");
        } else if (configName.equals(Configuration.EVALUATION_ENABLED_FINAL.name()) && config.isEnabled()) {
          Modal.display("Registrar Evaluacion-Final", "RegisterEvaluationModal");
        }
      }
      Modal.displayError("No se han habilitado las evaluaciones");

    } catch (SQLException e) {
      Modal.displayError("No fue posible recuperar las practicas profesionales");
    }
  }
}
