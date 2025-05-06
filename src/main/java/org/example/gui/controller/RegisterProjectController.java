package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class RegisterProjectController extends Controller {
  private final ProjectDAO PROJECT_DAO = new ProjectDAO();
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldMethodology;
  @FXML
  private TextField fieldSector;

  public void handleRegisterProject(ActionEvent event) {
    try {
      ProjectDTO dataObjectProject = new ProjectDTO.ProjectBuilder()
        .setIDOrganization(fieldEmail.getText())
        .setName(fieldName.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(fieldSector.getText())
        .build();

      PROJECT_DAO.createOne(dataObjectProject);
      AlertDialog.showSuccess("El proyecto ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible registrar el proyecto debido a un error de sistema.");
    }
  }

  public void navigateToProjectList() {
    ReviewProjectListController.navigateToProjectListPage(getScene());
  }
}
