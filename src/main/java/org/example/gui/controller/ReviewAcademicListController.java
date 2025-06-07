package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.business.dto.AcademicDTO;
import org.example.business.dao.AcademicDAO;
import org.example.gui.Modal;

import java.sql.SQLException;

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

  @FXML
  private void initialize() {
    loadTableColumns();
    loadAcademicList();
    loadRowDoubleClickHandler();
  }

  private void loadTableColumns() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastName.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastName.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
  }

  private void loadAcademicList() {
    try {
      tableAcademic.setItems(
        FXCollections.observableList(
          ACADEMIC_DAO.getAll()
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de académicos debido a un error de sistema."
      );
    }
  }

  private void loadRowDoubleClickHandler() {
    setRowDoubleClickHandler(
      tableAcademic,
      (academic) -> {
        navigateToManageAcademicPage(academic);
        return null;
      }
    );
  }

  public void handleOpenRegisterAcademicModal() {
    Modal.display(
      "Registrar Académico",
      "RegisterAcademicModal",
      this::loadAcademicList
    );
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
}
