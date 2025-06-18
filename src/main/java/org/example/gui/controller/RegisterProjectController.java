package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.ProjectSector;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class RegisterProjectController extends Controller {
  private final ProjectDAO PROJECT_DAO = new ProjectDAO();
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TextField fieldName;
  @FXML
  private TextArea fieldDescription;
  @FXML
  private TextField fieldDepartment;
  @FXML
  private TextField fieldAvailablePlaces;
  @FXML
  private ComboBox<OrganizationDTO> comboBoxOrganization;
  @FXML
  private TextField fieldMethodology;
  @FXML
  private ComboBox<ProjectSector> comboBoxSector;

  public void initialize() {
    loadComboBoxOrganization();
    loadComboBoxSector(comboBoxSector);
  }

  public static void loadComboBoxSector(ComboBox<ProjectSector> comboBoxSector) {
    comboBoxSector.getItems().addAll(ProjectSector.values());
    comboBoxSector.setValue(ProjectSector.PUBLIC);
  }

  public void loadComboBoxOrganization() {
    try {
      List<OrganizationDTO> organizationList = ORGANIZATION_DAO.getAllByState("ACTIVE");

      if (organizationList.isEmpty()) {
        Modal.displayError("No existe una organización. Por favor, registre una organización antes de registrar un proyecto.");
        return;
      }

      comboBoxOrganization.getItems().addAll(organizationList);
      comboBoxOrganization.setValue(organizationList.getFirst());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
    }
  }

  public void handleRegister() {
    try {
      ProjectDTO projectDTO = new ProjectDTO.ProjectBuilder()
        .setIDOrganization(comboBoxOrganization.getValue().getEmail())
        .setName(fieldName.getText())
        .setDescription(fieldDescription.getText())
        .setDepartment(fieldDepartment.getText())
        .setAvailablePlaces(fieldAvailablePlaces.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(comboBoxSector.getValue())
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
