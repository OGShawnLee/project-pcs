package org.example.gui.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import org.example.business.auth.AuthClient;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.*;
import org.example.business.dao.ProjectRequestDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: UPDATE USE CASE
public class RegisterProjectRequestController extends Controller {
  @FXML
  private TableView<ProjectDTO> projectRequestTable;
  @FXML
  private TableColumn<ProjectDTO, String> columnID;
  @FXML
  private TableColumn<ProjectDTO, String> columnEmail;
  @FXML
  private TableColumn<ProjectDTO, String> columnName;
  @FXML
  private TableColumn<ProjectDTO, String> columnMethodology;
  @FXML
  private TableColumn<ProjectDTO, String> columnDescription;
  @FXML
  private TableColumn<ProjectDTO, String> columnDepartment;
  @FXML
  private TableColumn<ProjectDTO, Integer> columnAvailablePlaces;
  @FXML
  private TableColumn<ProjectDTO, ProjectSector> columnSector;
  @FXML
  private TableColumn<ProjectDTO, String> columnState;
  @FXML
  private TableColumn<ProjectDTO, String> columnCreatedAt;
  @FXML
  private TableColumn<ProjectDTO, Boolean> checkBoxColumn;

  private final StudentDAO STUDENT_DAO = new StudentDAO();
  private final List<ProjectDTO> selectedProjects = new ArrayList<>();
  private final HashMap<ProjectDTO, BooleanProperty> selectionMap = new HashMap<>();
  private final ProjectRequestDAO PROJECT_REQUEST_DAO = new ProjectRequestDAO();


  public void initialize() {
    projectRequestTable.setEditable(true);
    checkBoxColumn.setEditable(true);

    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("IDOrganization"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnMethodology.setCellValueFactory(new PropertyValueFactory<>("methodology"));
    columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    columnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
    columnAvailablePlaces.setCellValueFactory(new PropertyValueFactory<>("availablePlaces"));
    columnSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));


    checkBoxColumn.setCellValueFactory(cellData -> {
      ProjectDTO projectDTO = cellData.getValue();
      BooleanProperty property = selectionMap.computeIfAbsent(projectDTO, project -> {
        BooleanProperty newProperty = new SimpleBooleanProperty(false);
        newProperty.addListener((observer, oldValue, newValue) -> {
          if (newValue) {
            if (selectedProjects.size() >= 3) {
              Platform.runLater(() -> newProperty.set(false));
            } else {
              selectedProjects.add(projectDTO);
            }
          } else {
            selectedProjects.remove(projectDTO);
          }
        });
        return newProperty;
      });
      return property;
    });

    checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

    loadProjects();
  }

  private void loadProjects() {
    try {
      List<ProjectDTO> projects = new ProjectDAO().getAll();
      if (projects.isEmpty() || projects.size() < 3) {
        AlertFacade.showErrorAndWait("No hay proyectos disponibles para mostrar.");
        return;
      }

      ObservableList<ProjectDTO> projectList = FXCollections.observableArrayList(projects);
      projectRequestTable.setItems(projectList);
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  public void handleRegister() {
    try {
      StudentDTO currentStudent = STUDENT_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());

      if (selectedProjects.size() != 3) {
        AlertFacade.showErrorAndWait("Debes seleccionar exactamente 3 proyectos.");
        return;
      }

      List<ProjectRequestDTO> existingRequests = PROJECT_REQUEST_DAO.getAllByEmail(currentStudent.getID());
      if (!existingRequests.isEmpty()) {
        AlertFacade.showErrorAndWait("Ya has realizado una solicitud de proyectos.");
        return;
      }

      for (ProjectDTO project : selectedProjects) {
        ProjectRequestDTO newRequest = new ProjectRequestDTO.ProjectRequestBuilder()
          .setIDStudent(currentStudent.getID())
          .setIDProject(project.getID())
          .setState("PENDING")
          .setReasonOfState("Solicitud inicial del estudiante")
          .setCreatedAt(java.time.LocalDateTime.now())
          .build();

        PROJECT_REQUEST_DAO.createOne(newRequest);
      }

      AlertFacade.showSuccessAndWait("La solicitud de proyectos fue registrada exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
