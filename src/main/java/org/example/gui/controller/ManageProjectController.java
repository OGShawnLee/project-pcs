package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class ManageProjectController extends ManageController<ProjectDTO> {
  private static final ProjectDAO PROJECT_DAO = new ProjectDAO();
  private static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TextField fieldIDProject;
  @FXML
  private TextField fieldName;
  @FXML
  private ComboBox<OrganizationDTO> comboBoxOrganization;
  @FXML
  private TextField fieldMethodology;
  @FXML
  private ComboBox<String> comboBoxSector;
  @FXML
  private ComboBox<String> comboBoxState;

  public void initialize(ProjectDTO currentProject) {
    super.initialize(currentProject);
    loadRecordState(comboBoxState);
    RegisterProjectController.loadComboBoxSector(comboBoxSector);
    loadComboBoxOrganization();
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldIDProject.setText(String.valueOf(getCurrentDataObject().getID()));

    try {
      for (OrganizationDTO organizationDTO : comboBoxOrganization.getItems()) {
        if (organizationDTO.getEmail().equals(getCurrentDataObject().getIDOrganization())) {
          comboBoxOrganization.setValue(organizationDTO);
          break;
        }
      }

      OrganizationDTO organization = ORGANIZATION_DAO.getOne(getCurrentDataObject().getIDOrganization());
      comboBoxOrganization.setValue(organization);
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar la organización del proyecto debido a un error en el sistema.");
    }

    fieldName.setText(getCurrentDataObject().getName());
    fieldMethodology.setText(getCurrentDataObject().getMethodology());
    comboBoxSector.setValue(getCurrentDataObject().getSector());
    comboBoxState.setValue(getCurrentDataObject().getState());
  }

  public void loadComboBoxOrganization() {
    try {
      List<OrganizationDTO> organizationList = ORGANIZATION_DAO.getAllByState("ACTIVE");

      if (organizationList.isEmpty()) {
        Modal.displayError("No existe una organización. Por favor, registre una organización antes de registrar un proyecto.");
        Modal.display("Registrar Organización", "RegisterProjectModal", this::loadComboBoxOrganization);
        return;
      }

      comboBoxOrganization.getItems().addAll(organizationList);
      comboBoxOrganization.setValue(organizationList.get(0));
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
    }
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      ProjectDTO updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(Integer.parseInt(fieldIDProject.getText()))
        .setIDOrganization(comboBoxOrganization.getValue().getEmail())
        .setName(fieldName.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(comboBoxSector.getValue())
        .setState(comboBoxState.getValue())
        .build();

      PROJECT_DAO.updateOne(updatedProject);
      Modal.displaySuccess("El proyecto ha sido actualizado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar proyecto debido a un error en el sistema.");
    }
  }

  public void navigateToProjectList() {
    ReviewProjectListController.navigateToProjectListPage(getScene());
  }
}
