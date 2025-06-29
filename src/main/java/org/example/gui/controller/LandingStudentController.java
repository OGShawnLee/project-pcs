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
import org.example.gui.Modal;

public class LandingStudentController extends LandingController {
  private StudentDTO currentStudentDTO;

  public void initialize() {
    super.initialize();
    Platform.runLater(() -> {
      try {
        this.currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
      } catch (UserDisplayableException e) {
        Modal.displayError("No ha sido posible cargar los datos del estudiante actual.", e);
        handleLogOut();
      }
    });
  }

  public void handleOpenReviewStudentProject() {
    Modal.display("Consultar Proyecto", "ReviewStudentProjectModal");
  }

  public void handleOpenReviewWorkPlan() {
    try {
      ProjectDTO currentProject = new ProjectDAO().getOneByStudent(currentStudentDTO.getID());

      if (currentProject == null) {
        Modal.displayError("No se ha encontrado un proyecto asignado al estudiante actual.");
        return;
      }

      WorkPlanDTO currentWorkPlan = new WorkPlanDAO().getOne(currentProject.getID());
      Modal.displayContextModal("Consultar Plan de Trabajo", "ReviewWorkPlanModal", currentWorkPlan);
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible cargar el plan de trabajo del estudiante actual.", e);
    } catch (NotFoundException e) {
      Modal.displayError("No se ha encontrado un plan de trabajo para el proyecto del estudiante actual.");
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