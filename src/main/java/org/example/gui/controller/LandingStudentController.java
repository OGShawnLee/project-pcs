package org.example.gui.controller;

import javafx.application.Platform;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class LandingStudentController extends LandingController {
  private StudentDTO currentStudentDTO;

  public void initialize() {
    super.initialize();
    Platform.runLater(() -> {
      try {
        this.currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
      } catch (NotFoundException | UserDisplayableException e) {
        AlertFacade.showErrorAndWait("No ha sido posible cargar los datos del estudiante actual.", e.getMessage());
        handleLogOut();
      }
    });
  }

  public void handleOpenReviewStudentProject() {
    ReviewStudentProjectController.displayAsContextModal(currentStudentDTO);
  }

  public void handleOpenReviewWorkPlan() {
    ReviewWorkPlanController.displayAsContextModal(currentStudentDTO);
  }

  public void handleOpenRegisterProjectRequest() {
    navigateFromThisPageTo("Registrar Solicitud de Proyecto", "RegisterProjectRequestModal");
  }

  public void handleOpenRegisterSelfEvaluation() {
    navigateFromThisPageTo("Registrar Autoevaluación", "RegisterSelfEvaluationPage");
  }

  public void handleOpenRegisterMonthlyReport() {
    navigateFromThisPageTo("Registrar Informe Mensual", "RegisterMonthlyReportPage");
  }
}