package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dto.AcademicDTO;
import org.example.business.dao.AcademicDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;
import java.util.List;

public class ReviewAcademicListController extends Controller {
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

  public void navigateToRegisterAcademicPage() {
    navigateFromThisPageTo("Registrar Académico", "RegisterAcademicPage");
  }

  public static void navigateToAcademicListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Académicos", "ReviewAcademicListPage");
  }

  public void navigateToManageAcademicPage(AcademicDTO currentAcademic) {
    navigateToManagePage(
      getScene(),
      "Gestionar Académico",
      "ManageAcademicPage",
      currentAcademic
    );
  }

  @FXML
  private void initialize() {
    loadAcademicList();
    setRowDoubleClickHandler(
      tableAcademic,
      academic -> {
        navigateToManageAcademicPage(academic);
        return null;
      }
    );
  }
}
