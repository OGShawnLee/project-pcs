package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ReviewStudentListController extends ReviewListController implements FilterableByStateController {
  private static final StudentDAO STUDENT_DAO = new StudentDAO();
  @FXML
  private TableView<StudentDTO> tableStudent;
  @FXML
  private TableColumn<StudentDTO, String> columnID;
  @FXML
  private TableColumn<StudentDTO, String> columnPaternalLastName;
  @FXML
  private TableColumn<StudentDTO, String> columnMaternalLastName;
  @FXML
  private TableColumn<StudentDTO, String> columnName;
  @FXML
  private TableColumn<StudentDTO, String> columnEmail;
  @FXML
  private TableColumn<StudentDTO, String> columnCreatedAt;
  @FXML
  private TableColumn<StudentDTO, String> columnFinalGrade;
  @FXML
  private TableColumn<StudentDTO, String> columnPhoneNumber;
  @FXML
  private TableColumn<StudentDTO, String> columnState;
  @Override
  public void loadTableColumns() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastName.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastName.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    columnFinalGrade.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
    columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
  }

  @Override
  public void loadDataList() {
    try {
      tableStudent.setItems(
        FXCollections.observableList(
          STUDENT_DAO.getAll()
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de estudiantes debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByActiveState() {
    try {
      tableStudent.setItems(
        FXCollections.observableList(
          STUDENT_DAO.getAllByState("ACTIVE")
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de estudiantes activos debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByInactiveState() {
    try {
      tableStudent.setItems(
        FXCollections.observableList(
          STUDENT_DAO.getAllByState("RETIRED")
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de estudiantes inactivos debido a un error de sistema."
      );
    }
  }

  public void handleOpenRegisterStudentModal() {
    Modal.display(
      "Registrar Student",
      "RegisterStudentModal",
      this::loadDataList
    );
  }

  public void handleManageStudent() {
    StudentDTO selectedStudent = tableStudent.getSelectionModel().getSelectedItem();

    if (selectedStudent == null) return;

    Modal.displayManageModal(
      "Gestionar Estudiante",
      "ManageStudentModal",
      this::loadDataList,
      selectedStudent
    );
  }

  public static void navigateToStudentListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Estudiantes", "ReviewStudentListPage");
  }
}
