package org.example.business.controller;

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
import org.example.business.dto.AcademicDTO;
import org.example.db.dao.AcademicDAO;
import org.example.gui.AlertDialog;
import org.example.gui.controller.LandingPageController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ReviewAcademicListController {
  private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  @FXML
  private TableView<AcademicDTO> tableAcademic;
  @FXML
  private TableColumn<AcademicDTO, String> columnID;
  @FXML
  private TableColumn<AcademicDTO, String> columnEmail;
  @FXML
  private TableColumn<AcademicDTO, String> columnName;
  @FXML
  private TableColumn<AcademicDTO, String> columnPaternalLastName;
  @FXML
  private TableColumn<AcademicDTO, String> columnMaternalLastName;
  @FXML
  private TableColumn<AcademicDTO, String> columnRole;
  @FXML
  private TableColumn<AcademicDTO, String> columnState;
  @FXML
  private TableColumn<AcademicDTO, String> columnCreatedAt;

  private void loadAcademicList() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnPaternalLastName.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastName.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

    try {
      List<AcademicDTO> academicList = ACADEMIC_DAO.getAll();
      ObservableList<AcademicDTO> observableAcademicList = FXCollections.observableList(academicList);
      tableAcademic.setItems(observableAcademicList);
    } catch (SQLException e) {
      AlertDialog.showError(
        "No ha sido posible cargar información de académicos debido a un error de sistema."
      );
    }
  }

  public void navigateToLandingPage() {
    try {
      LandingPageController.navigateToLandingPage(
        (Stage) tableAcademic.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de inicio."
      );
    }
  }

  public void navigateToRegisterAcademicPage() {
    try {
      RegisterAcademicController.navigateToRegisterAcademicPage(
        (Stage) tableAcademic.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de registro de académicos."
      );
    }
  }

  public static void navigateToAcademicListPage(Stage currentStage) throws IOException {
    Parent newView = FXMLLoader.load(Objects.requireNonNull(ReviewAcademicListController.class.getResource("/org/example/ReviewAcademicListPage.fxml")));
    Scene newScene = new Scene(newView);

    currentStage.setScene(newScene);
    currentStage.show();
  }

  @FXML
  private void initialize() {
    loadAcademicList();
  }
}
