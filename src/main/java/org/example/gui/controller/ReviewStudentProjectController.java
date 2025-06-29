package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.example.business.auth.AuthClient;
import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

public class ReviewStudentProjectController extends Controller {
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

  public void initialize() {
    loadStudentProject();
  }

  public void loadStudentProject() {
    try {
      StudentDTO currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
      ProjectDTO projectDTO = new ProjectDAO().getOneByStudent(currentStudentDTO.getID());
      OrganizationDTO organizationDTO = new OrganizationDAO().getOne(projectDTO.getIDOrganization());

      fieldIDProject.setText(String.valueOf(projectDTO.getID()));
      fieldName.setText(projectDTO.getName());
      fieldDescription.setText(projectDTO.getDescription());
      fieldOrganization.setText(organizationDTO.getName());
      fieldMethodology.setText(projectDTO.getMethodology());
      fieldSector.setText(projectDTO.getSector().toString());
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible consultar el proyecto asignado.", e);
    }
  }
}