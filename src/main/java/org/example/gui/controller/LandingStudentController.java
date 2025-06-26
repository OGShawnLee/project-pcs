package org.example.gui.controller;

import org.example.gui.Modal;

public class LandingStudentController extends LandingController {
  public void handleOpenReviewStudentProject() {
    Modal.display("Consultar Proyecto", "ReviewStudentProjectModal");
  }

  public void handleOpenRegisterProjectRequest() {
    navigateFromThisPageTo("Registrar Solicitud de Proyecto", "RegisterProjectRequestModal");
  }

  public void handleOpenManageProfile() {
    navigateFromThisPageTo("Gestionar Perfil", "ManageProfilePage");
  }

  public void handleOpenRegisterSelfEvaluation() {
    navigateFromThisPageTo("Registrar Autoevaluación", "RegisterSelfEvaluationPage");
  }

  public void handleOpenRegisterMonthlyReport() {
    navigateFromThisPageTo("Registrar Informe Mensual", "RegisterMonthlyReportPage");
  }
}