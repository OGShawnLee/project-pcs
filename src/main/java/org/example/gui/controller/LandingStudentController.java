package org.example.gui.controller;

import javafx.application.Platform;
import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dao.ProjectDAO;
import org.example.business.dao.WorkPlanDAO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.WorkPlanDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class LandingStudentController extends LandingController {
  private StudentDTO currentStudentDTO;

  public void initialize() {
    super.initialize();
    Platform.runLater(() -> {
      try {
        this.currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
      } catch (SQLException e) {
        Modal.displayError("No ha sido posible cargar los datos del estudiante actual debido a un error de sistema.");
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
    } catch (SQLException e) {
      e.printStackTrace();
      Modal.displayError("No ha sido posible cargar el plan de trabajo del estudiante actual debido a un error de sistema.");
    } catch (NotFoundException e) {
      Modal.displayError(e.getMessage());
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