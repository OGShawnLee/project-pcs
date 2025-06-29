package org.example.gui.controller;

import org.example.business.dto.enumeration.ConfigurationName;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dao.PracticeDAO;
import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.PracticeDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

import java.util.List;

public class LandingEvaluatorController extends LandingController {
  public void navigateToReviewEvaluationListPage() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Lista de Evaluaciones", "ReviewEvaluationListPage")
    );
  }

  public void handleOpenRegisterEvaluation() {
    try {
      List<PracticeDTO> practices = new PracticeDAO().getAll();

      if (practices.isEmpty()) {
        AlertFacade.showErrorAndWait("No existen prácticas. Registre una antes de evaluar.");
        return;
      }

      List<ConfigurationDTO> currentConfiguration = new ConfigurationDAO().getAll();

      for (ConfigurationDTO config : currentConfiguration) {
        String configName = String.valueOf(config.name());
        if (configName.equals(ConfigurationName.EVALUATION_ENABLED_FIRST.name()) && config.isEnabled()) {
          ModalFacade.createAndDisplay(
            new ModalFacadeConfiguration("Registrar Evaluación - 1er Parcial", "RegisterEvaluationModal")
          );
        } else if (configName.equals(ConfigurationName.EVALUATION_ENABLED_SECOND.name()) && config.isEnabled()) {
          ModalFacade.createAndDisplay(
            new ModalFacadeConfiguration("Registrar Evaluación - 2do Parcial", "RegisterEvaluationModal")
          );
        } else if (configName.equals(ConfigurationName.EVALUATION_ENABLED_FINAL.name()) && config.isEnabled()) {
          ModalFacade.createAndDisplay(
            new ModalFacadeConfiguration("Registrar Evaluación - Final", "RegisterEvaluationModal")
          );
        }
      }

      AlertFacade.showErrorAndWait("No se han habilitado las evaluaciones.");
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}