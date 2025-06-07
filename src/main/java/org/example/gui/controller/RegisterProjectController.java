package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.gui.Modal;

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

  public void handleRegister() {
    try {
      ProjectDTO projectDTO = new ProjectDTO.ProjectBuilder()
        .setIDOrganization(fieldEmail.getText())
        .setName(fieldName.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(fieldSector.getText())
        .build();

      PROJECT_DAO.createOne(projectDTO);
      Modal.displaySuccess("El proyecto ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible registrar proyecto debido a un error en el sistema.");
    }
  }
}
