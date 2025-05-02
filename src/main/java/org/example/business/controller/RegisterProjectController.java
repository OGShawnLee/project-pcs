package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.Result;
import org.example.business.dto.ProjectDTO;
import org.example.business.validation.Validator;
import org.example.db.dao.ProjectDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class RegisterProjectController {
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
    ProjectDTO project = new ProjectDTO.ProjectBuilder()
      .setIDOrganization(fieldEmail.getText())
      .setName(fieldName.getText())
      .setMethodology(fieldMethodology.getText())
      .setSector(fieldSector.getText())
      .build();

    Result<String> nameResult = Validator.getFilledString(
      project.getName(),
      "Nombre no puede estar vacío"
    );

    if (nameResult.isFailure()) {
      AlertDialog.showError(nameResult.getError());
      return;
    }

    Result<String> emailResult = Validator.getEmail(
      project.getIDOrganization(),
      "Correo electrónico de la organización no es valido"
    );

    if (emailResult.isFailure()) {
      AlertDialog.showError(emailResult.getError());
      return;
    }

    Result<String> methodologyResult = Validator.getFilledString(
      project.getMethodology(),
      "Nombre del representante no puede estar vacío"
    );

    if (methodologyResult.isFailure()) {
      AlertDialog.showError(methodologyResult.getError());
      return;
    }

    Result<String> sectorResult = Validator.getFilledString(
      project.getSector(),
      "El sector no puede estar vacío"
    );

    if (sectorResult.isFailure()) {
      AlertDialog.showError(sectorResult.getError());
      return;
    }

    try {
      PROJECT_DAO.createOne(project);
      AlertDialog.showSuccess("El proyecto ha sido registrado exitosamente.");
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible registrar el proyecto debido a un error de sistema.");
    }
  }
}
