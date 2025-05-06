package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.ProjectDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ManageProjectController extends ManageController<ProjectDTO> {
  private final ProjectDAO PROJECT_DAO = new ProjectDAO();
  @FXML
  private TextField fieldIDProject;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldMethodology;
  @FXML
  private ComboBox<String> fieldSector;
  @FXML
  private ComboBox<String> fieldState;

  public void initialize(ProjectDTO currentProject) {
    fieldIDProject.setText(Integer.toString(currentProject.getID()));
    fieldName.setText(currentProject.getName());
    fieldEmail.setText(currentProject.getIDOrganization());
    fieldMethodology.setText(currentProject.getMethodology());
    fieldSector.setValue(currentProject.getSector());
    fieldState.setValue(currentProject.getState());
    fieldSector.getItems().addAll("Público", "Privado", "Social");
    fieldState.getItems().addAll("Activo", "Inactivo");
    fieldIDProject.setEditable(false);
  }

  @Override
  @FXML
  protected void handleUpdateCurrentDataObject() {
    try {
      ProjectDTO updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(Integer.parseInt(fieldIDProject.getText()))
        .setIDOrganization(fieldEmail.getText())
        .setName(fieldName.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(fieldSector.getValue())
        .setState(fieldState.getValue())
        .build();

      PROJECT_DAO.updateOne(updatedProject);
      Modal.displaySuccess("El proyecto ha sido actualizado con éxito.");
      navigateToProjectList();
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar el proyecto debido a un error de sistema.");
    }
  }

  public void navigateToProjectList() {
    ReviewProjectListController.navigateToProjectListPage(getScene());
  }
}
