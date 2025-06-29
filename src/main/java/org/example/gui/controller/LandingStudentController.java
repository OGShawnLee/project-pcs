package org.example.gui.controller;

import javafx.application.Platform;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dao.ProjectDAO;
import org.example.business.dao.WorkPlanDAO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.WorkPlanDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class LandingStudentController extends LandingController {
  private StudentDTO currentStudentDTO;

  public void initialize() {
    super.initialize();
    Platform.runLater(() -> {
      try {
        this.currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
      } catch (UserDisplayableException e) {
        AlertFacade.showErrorAndWait("No ha sido posible cargar los datos del estudiante actual.", e);
        handleLogOut();
      }
    });
  }

  public void handleOpenReviewStudentProject() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Consultar Proyecto del Estudiante", "ReviewStudentProjectModal")
    );
  }

  public void handleOpenReviewWorkPlan() {
    try {
      ProjectDTO currentProject = new ProjectDAO().getOneByStudent(currentStudentDTO.getID());

      if (currentProject == null) {
        AlertFacade.showErrorAndWait("No se ha encontrado un proyecto asignado al estudiante actual.");
        return;
      }

      WorkPlanDTO currentWorkPlan = new WorkPlanDAO().getOne(currentProject.getID());

      ModalFacade.createAndDisplayContextModal(
        new ModalFacadeConfiguration("Consultar Plan de Trabajo", "ReviewWorkPlanModal"),
        currentWorkPlan
      );
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar el plan de trabajo del estudiante actual.", e);
    } catch (NotFoundException e) {
      AlertFacade.showErrorAndWait("No se ha encontrado un plan de trabajo para el proyecto del estudiante actual.");
    }
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