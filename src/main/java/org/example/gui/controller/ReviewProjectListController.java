package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class ReviewProjectListController extends ReviewListController {
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
    createContextMenuHandler();
  }

  @Override
  public void loadTableColumns() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("IDOrganization"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnMethodology.setCellValueFactory(new PropertyValueFactory<>("methodology"));
    columnSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
  }

  @Override
  public void loadDataList() {
    try {
      tableProject.setItems(
        FXCollections.observableList(
          PROJECT_DAO.getAll()
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de proyectos debido a un error en el sistema."
      );
    }
  }

  public void navigateToAssignPage(ProjectDTO currentProject) {
    navigateToManagePage(
      getScene(),
      "Asignar Proyecto",
      "AssignProjectPage",
      currentProject
    );
  }

  public void handleOpenRegisterProjectModal() {
    Modal.display(
      "Registrar Proyecto",
      "RegisterProjectModal",
      this::loadDataList
    );
  }

  public static void navigateToProjectListPage(Stage currentStage) {
    navigateTo(currentStage, "Listado de Proyectos" , "ReviewProjectListPage");
  }

  public void navigateToManageProjectPage(ProjectDTO currentProject) {
    navigateToManagePage(
      getScene(),
      "Modificar Proyecto",
      "ManageProjectPage",
      currentProject
    );
  }

  private void createContextMenuHandler() {
    ContextMenu menu = new ContextMenu();

    MenuItem itemAssign = new MenuItem("Asignar Proyecto");
    itemAssign.setOnAction(event -> {
      ProjectDTO currentProject = tableProject.getSelectionModel().getSelectedItem();
      if (currentProject != null) {
        navigateToAssignPage(currentProject);
      }
    });

    MenuItem itemEdit = new MenuItem("Gestionar Proyecto");
    itemEdit.setOnAction(event -> {
      ProjectDTO currentProject = tableProject.getSelectionModel().getSelectedItem();
      if (currentProject != null) {
        navigateToManageProjectPage(currentProject);
      }
    });

    menu.getItems().addAll(itemAssign, itemEdit);

    tableProject.setOnContextMenuRequested(event -> {
      ProjectDTO currentProject = tableProject.getSelectionModel().getSelectedItem();
      if (currentProject != null) {
        menu.show(tableProject, event.getScreenX(), event.getScreenY());
      } else {
        menu.hide();
      }
    });
  }
}
