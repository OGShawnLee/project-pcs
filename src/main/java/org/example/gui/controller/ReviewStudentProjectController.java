package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.example.business.dao.NotFoundException;
import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class ReviewStudentProjectController extends ContextController<ProjectDTO> {
  private static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private Text fieldIDProject;
  @FXML
  private Text fieldName;
  @FXML
  private Text fieldDescription;
  @FXML
  private Text fieldOrganization;
  @FXML
  private Text fieldMethodology;
  @FXML
  private Text fieldSector;

  @Override
  public void initialize(ProjectDTO projectDTO) {
    super.initialize(projectDTO);
    loadStudentProject();
  }

  private void loadFields(OrganizationDTO organizationDTO) {
    fieldIDProject.setText(String.valueOf(getContext().getID()));
    fieldName.setText(getContext().getName());
    fieldDescription.setText(getContext().getDescription());
    fieldOrganization.setText(organizationDTO.getName());
    fieldMethodology.setText(getContext().getMethodology());
    fieldSector.setText(getContext().getSector().toString());
  }

  public void loadStudentProject() {
    try {
      loadFields(ORGANIZATION_DAO.getOne(getContext().getIDOrganization()));
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible consultar el proyecto asignado.", e.getMessage());
    }
  }

  public static void displayAsContextModal(StudentDTO currentStudentDTO) {
    try {
      ProjectDTO currentProjectDTO = new ProjectDAO().getOneByStudent(currentStudentDTO.getID());

      ModalFacade.createAndDisplayContextModal(
        new ModalFacadeConfiguration("Consultar Proyecto del Estudiante", "ReviewStudentProjectModal"),
        currentProjectDTO
      );
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar el proyecto asignado.", e.getMessage());
    }
  }
}