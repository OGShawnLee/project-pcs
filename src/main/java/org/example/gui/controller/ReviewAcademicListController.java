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

public class ReviewAcademicListController extends ReviewListController implements FilterableByStateController {
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
  private TableColumn<AcademicDTO, String> columnPhoneNumber;
  @FXML
  private TableColumn<AcademicDTO, String> columnRole;
  @FXML
  private TableColumn<AcademicDTO, String> columnState;
  @FXML
  private TableColumn<AcademicDTO, String> columnCreatedAt;

  @Override
  public void loadTableColumns() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastName.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastName.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
  }

  @Override
  public void loadDataList() {
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

  @Override
  public void loadDataListByActiveState() {
    try {
      tableAcademic.setItems(
        FXCollections.observableList(
          ACADEMIC_DAO.getAllByState("ACTIVE")
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de académicos activos debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByInactiveState() {
    try {
      tableAcademic.setItems(
        FXCollections.observableList(
          ACADEMIC_DAO.getAllByState("RETIRED")
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de académicos inactivos debido a un error de sistema."
      );
    }
  }

  public void handleOpenRegisterAcademicModal() {
    Modal.display(
      "Registrar Académico",
      "RegisterAcademicModal",
      this::loadDataList
    );
  }

  public void handleManageAcademic() {
    AcademicDTO selectedAcademic = tableAcademic.getSelectionModel().getSelectedItem();

    if (selectedAcademic == null) return;

    Modal.displayManageModal(
      "Gestionar Académico",
      "ManageAcademicModal",
      this::loadDataList,
      selectedAcademic
    );
  }

  public static void navigateToAcademicListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Académicos", "ReviewAcademicListPage");
  }
}
