package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.example.business.dto.RepresentativeDTO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.WorkPlanDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

// TODO: UPDATE USE CASE
public class RegisterProjectController extends Controller {
  private final ProjectDAO PROJECT_DAO = new ProjectDAO();
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
  private ComboBox<RepresentativeDTO> comboBoxRepresentative;
  @FXML
  private TextField fieldMethodology;
  @FXML
  private ComboBox<ProjectSector> comboBoxSector;
  @FXML
  private TextArea fieldProjectGoal;
  @FXML
  private TextArea fieldTheoreticalScope;
  @FXML
  private TextArea fieldFirstMonthActivities;
  @FXML
  private TextArea fieldSecondMonthActivities;
  @FXML
  private TextArea fieldThirdMonthActivities;
  @FXML
  private TextArea fieldFourthMonthActivities;

  public void initialize() {
    ComboBoxLoader.loadComboBoxOrganizationWithRepresentatives(comboBoxOrganization);
    ComboBoxLoader.loadComboBoxSector(comboBoxSector);
    ComboBoxLoader.loadComboBoxRepresentativeByOrganization(comboBoxRepresentative, comboBoxOrganization.getItems().getFirst().getEmail());
    ComboBoxLoader.loadSynchronizationOfRepresentativeComboBoxFromOrganizationComboBoxSelection(comboBoxOrganization, comboBoxRepresentative);

    fieldDescription.textProperty().addListener((observable, oldValue, newValue) -> {
      fieldDescription.setPrefHeight(fieldDescription.getPrefRowCount() * 24); // Adjust height based on rows
    });
  }

  public void handleRegister() {
    try {
      ProjectDTO projectDTO = new ProjectDTO.ProjectBuilder()
        .setIDOrganization(comboBoxOrganization.getValue().getEmail())
        .setRepresentativeEmail(comboBoxRepresentative.getValue().getEmail())
        .setName(fieldName.getText())
        .setDescription(fieldDescription.getText())
        .setDepartment(fieldDepartment.getText())
        .setAvailablePlaces(fieldAvailablePlaces.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(comboBoxSector.getValue())
        .build();
      WorkPlanDTO workPlanDTO = new WorkPlanDTO.WorkPlanBuilder()
        .setProjectGoal(fieldProjectGoal.getText())
        .setTheoreticalScope(fieldTheoreticalScope.getText())
        .setFirstMonthActivities(fieldFirstMonthActivities.getText())
        .setSecondMonthActivities(fieldSecondMonthActivities.getText())
        .setThirdMonthActivities(fieldThirdMonthActivities.getText())
        .setFourthMonthActivities(fieldFourthMonthActivities.getText())
        .build();

      PROJECT_DAO.createOneWithWorkPlan(projectDTO, workPlanDTO);
      Modal.displaySuccess("El proyecto ha sido registrado exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }
}
