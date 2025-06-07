package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dao.OrganizationDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class ReviewOrganizationListController extends Controller {
  private static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TableView<OrganizationDTO> tableOrganization;
  @FXML
  private TableColumn<OrganizationDTO, String> columnEmail;
  @FXML
  private TableColumn<OrganizationDTO, String> columnName;
  @FXML
  private TableColumn<OrganizationDTO, String> columnRepresentativeFullName;
  @FXML
  private TableColumn<OrganizationDTO, String> columnColony;
  @FXML
  private TableColumn<OrganizationDTO, String> columnStreet;
  @FXML
  private TableColumn<OrganizationDTO, String> columnCreatedAt;

  public void initialize() {
    loadTableColumns();
    loadProjectList();
    loadRowDoubleClickHandler();
  }

  public void loadTableColumns() {
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnRepresentativeFullName.setCellValueFactory(new PropertyValueFactory<>("representativeFullName"));
    columnColony.setCellValueFactory(new PropertyValueFactory<>("colony"));
    columnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
  }

  private void loadProjectList() {
    try {
      tableOrganization.setItems(
        FXCollections.observableList(
          ORGANIZATION_DAO.getAll()
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de organizaciones debido a un error en el sistema."
      );
    }
  }

  private void loadRowDoubleClickHandler() {
    setRowDoubleClickHandler(
      tableOrganization,
      organization -> {
        navigateToManageOrganizationPage(organization);
        return null;
      }
    );
  }

  public void handleOpenRegisterOrganizationModal() {
    Modal.display(
      "Registrar Organización",
      "RegisterOrganizationModal",
      this::loadProjectList
    );
  }

  public static void navigateToOrganizationListPage(Stage currentStage) {
    navigateTo(currentStage, "Listado de Acádemicos", "ReviewOrganizationListPage");
  }

  public void navigateToManageOrganizationPage(OrganizationDTO currentOrganization) {
    navigateToManagePage(
      getScene(),
      "Gestionar Organización",
      "ManageOrganizationPage",
      currentOrganization
    );
  }
}
