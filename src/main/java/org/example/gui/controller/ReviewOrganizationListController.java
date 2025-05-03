package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.controller.RegisterOrganizationController;
import org.example.business.dto.OrganizationDTO;
import org.example.db.dao.OrganizationDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ReviewOrganizationListController {
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

  private void loadProjectList() {
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnRepresentativeFullName.setCellValueFactory(new PropertyValueFactory<>("representativeFullName"));
    columnColony.setCellValueFactory(new PropertyValueFactory<>("colony"));
    columnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

    try {
      List<OrganizationDTO> projectList = ORGANIZATION_DAO.getAll();
      ObservableList<OrganizationDTO> observableOrganizationList = FXCollections.observableList(projectList);
      tableOrganization.setItems(observableOrganizationList);
    } catch (SQLException e) {
      AlertDialog.showError(
        "No ha sido posible cargar información de académicos debido a un error de sistema."
      );
    }
  }

  public void navigateToLandingPage() {
    try {
      LandingPageController.navigateToLandingPage(
        (Stage) tableOrganization.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de inicio."
      );
    }
  }

  public void navigateToRegisterOrganizationPage() {
    try {
      RegisterOrganizationController.navigateToRegisterOrganizationPage(
        (Stage) tableOrganization.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de registro de proyectos."
      );
    }
  }

  public static void navigateToOrganizationListPage(Stage currentStage) throws IOException {
    Parent newView = FXMLLoader.load(Objects.requireNonNull(ReviewProjectListController.class.getResource("/org/example/ReviewOrganizationListPage.fxml")));
    Scene newScene = new Scene(newView);

    currentStage.setScene(newScene);
    currentStage.show();
  }

  @FXML
  private void initialize() {
    loadProjectList();
  }
}
