package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

public class ReviewProjectListController extends ReviewListController implements FilterableByStateController {
  private static final ProjectDAO PROJECT_DAO = new ProjectDAO();
  @FXML
  private TableView<ProjectDTO> tableProject;
  @FXML
  private TableColumn<ProjectDTO, String> columnID;
  @FXML
  private TableColumn<ProjectDTO, String> columnEmail;
  @FXML
  private TableColumn<ProjectDTO, String> columnName;
  @FXML
  private TableColumn<ProjectDTO, String> columnDescription;
  @FXML
  private TableColumn<ProjectDTO, String> columnDepartment;
  @FXML
  private TableColumn<ProjectDTO, Integer> columnAvailablePlaces;
  @FXML
  private TableColumn<ProjectDTO, String> columnMethodology;
  @FXML
  private TableColumn<ProjectDTO, String> columnSector;
  @FXML
  private TableColumn<ProjectDTO, String> columnState;
  @FXML
  private TableColumn<ProjectDTO, String> columnCreatedAt;

  @Override
  public void initialize() {
    loadDataList();
    loadTableColumns();
  }

  @Override
  public void loadTableColumns() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("IDOrganization"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnMethodology.setCellValueFactory(new PropertyValueFactory<>("methodology"));
    columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    columnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
    columnAvailablePlaces.setCellValueFactory(new PropertyValueFactory<>("availablePlaces"));
    columnSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
  }

  @Override
  public void loadDataList() {
    try {
      tableProject.setItems(
        FXCollections.observableList(
          PROJECT_DAO.getAll()
        )
      );
    } catch (UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }

  @Override
  public void loadDataListByActiveState() {
    try {
      tableProject.setItems(
        FXCollections.observableList(
          PROJECT_DAO.getAllByState("ACTIVE")
        )
      );
    } catch (UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }

  @Override
  public void loadDataListByInactiveState() {
    try {
      tableProject.setItems(
        FXCollections.observableList(
          PROJECT_DAO.getAllByState("INACTIVE")
        )
      );
    } catch (UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }

  public void handleOpenRegisterProjectModal() {
    Modal.display(
      "Registrar Proyecto",
      "RegisterProjectModal",
      this::loadDataList
    );
  }

  public void handleManageProject() {
    ProjectDTO selectedProject = tableProject.getSelectionModel().getSelectedItem();

    if (selectedProject == null) return;

    Modal.displayContextModal(
      "Gestionar Projecto",
      "ManageProjectModal",
      this::loadDataList,
      selectedProject
    );
  }

  public void handleManageProjectPractice() {
    ProjectDTO selectedProject = tableProject.getSelectionModel().getSelectedItem();

    if (selectedProject == null) return;

    Modal.displayContextModal(
      "Gestionar Proyecto",
      "AssignProjectPage",
      this::loadDataList,
      selectedProject
    );
  }

  public static void navigateToProjectListPage(Stage currentStage) {
    navigateTo(currentStage, "Listado de Proyectos", "ReviewProjectListPage");
  }
}
