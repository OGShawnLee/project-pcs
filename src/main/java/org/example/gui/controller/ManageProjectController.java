package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.example.business.dao.NotFoundException;
import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.RepresentativeDTO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

// TODO: UPDATE USE CASE
public class ManageProjectController extends ManageController<ProjectDTO> {
  private static final ProjectDAO PROJECT_DAO = new ProjectDAO();
  private static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TextField fieldIDProject;
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
  private ComboBox<String> comboBoxState;

  public void initialize(ProjectDTO currentProject) {
    super.initialize(currentProject);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    ComboBoxLoader.loadComboBoxState(comboBoxState);
    ComboBoxLoader.loadComboBoxSector(comboBoxSector);
    ComboBoxLoader.loadComboBoxOrganizationWithRepresentatives(comboBoxOrganization);
    ComboBoxLoader.loadComboBoxRepresentativeByOrganization(comboBoxRepresentative, comboBoxOrganization.getValue().getEmail());
    ComboBoxLoader.loadSynchronizationOfRepresentativeComboBoxFromOrganizationComboBoxSelection(comboBoxOrganization, comboBoxRepresentative);

    loadOrganization();
    loadRepresentative();

    fieldIDProject.setText(String.valueOf(getContext().getID()));
    fieldName.setText(getContext().getName());
    fieldDescription.setText(getContext().getDescription());
    fieldDepartment.setText(getContext().getDepartment());
    fieldAvailablePlaces.setText(String.valueOf(getContext().getAvailablePlaces()));
    fieldMethodology.setText(getContext().getMethodology());
    comboBoxSector.setValue(getContext().getSector());
    comboBoxState.setValue(getContext().getState());
  }

  private void loadOrganization() {
    try {
      for (OrganizationDTO organizationDTO : comboBoxOrganization.getItems()) {
        if (organizationDTO.getEmail().equals(getContext().getIDOrganization())) {
          comboBoxOrganization.setValue(organizationDTO);
          return;
        }
      }

      OrganizationDTO organization = ORGANIZATION_DAO.getOne(getContext().getIDOrganization());
      comboBoxOrganization.setValue(organization);
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  private void loadRepresentative() {
    try {
      for (RepresentativeDTO representativeDTO : comboBoxRepresentative.getItems()) {
        if (representativeDTO.getEmail().equals(getContext().getRepresentativeEmail())) {
          comboBoxRepresentative.setValue(representativeDTO);
          return;
        }
      }

      RepresentativeDTO representative = new RepresentativeDAO().getOne(getContext().getRepresentativeEmail());
      comboBoxRepresentative.setValue(representative);
    } catch (UserDisplayableException | NotFoundException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  private ProjectDTO createProjectDTOFromFields() {
    return new ProjectDTO.ProjectBuilder()
      .setID(Integer.parseInt(fieldIDProject.getText()))
      .setIDOrganization(comboBoxOrganization.getValue().getEmail())
      .setRepresentativeEmail(comboBoxRepresentative.getValue().getEmail())
      .setName(fieldName.getText())
      .setDescription(fieldDescription.getText())
      .setDepartment(fieldDepartment.getText())
      .setAvailablePlaces(fieldAvailablePlaces.getText())
      .setMethodology(fieldMethodology.getText())
      .setSector(comboBoxSector.getValue())
      .setState(comboBoxState.getValue())
      .build();
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      ProjectDTO updateProjectDTO = createProjectDTOFromFields();

      String organizationEmail = updateProjectDTO.getIDOrganization();
      String representativeOrganizationEmail = comboBoxRepresentative.getValue().getEmail();

      if (organizationEmail.equals(representativeOrganizationEmail)) {
        PROJECT_DAO.updateOne(updateProjectDTO);
        AlertFacade.showSuccessAndWait("El proyecto ha sido actualizado exitosamente.");
      }

      AlertFacade.showErrorAndWait("El representante debe pertenecer a la misma organización del proyecto.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
