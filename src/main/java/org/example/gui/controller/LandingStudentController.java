package org.example.gui.controller;

public class LandingStudentController extends LandingController {
  public void handleOpenReviewStudentProject() {
    navigateFromThisPageTo("Listado de Proyectos", "ReviewStudentProjectPage");
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