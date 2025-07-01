package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.business.dao.NotFoundException;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.combobox.CourseComboBoxLoader;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

import java.util.List;
import java.util.Optional;

public class ReviewStudentListController extends ReviewListController implements FilterableByStateController {
  private static final StudentDAO STUDENT_DAO = new StudentDAO();
  @FXML
  private ComboBox<CourseDTO> comboBoxCourse;
  @FXML
  private TableView<StudentDTO> tableStudent;
  @FXML
  private TableColumn<StudentDTO, String> columnID;
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
  public void initialize() {
    super.initialize();
    try {
      CourseComboBoxLoader.loadByCurrentAcademicDTO(comboBoxCourse); // Note: Must be loaded first.
      loadStudentListBasedOnCourseSelectionAndState(null);
      setLoadStudentListOnCourseSelectionAction();
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No es posible cargar los estudiantes.", e.getMessage());
    }
  }

  public void loadTableColumns() {
    columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    columnFinalGrade.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
    columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
  }

  @Override
  public void loadDataList() {
    loadStudentListBasedOnCourseSelectionAndState(null);
  }

  public Optional<CourseDTO> getSelectedCourse() {
    return Optional.ofNullable(comboBoxCourse.getSelectionModel().getSelectedItem());
  }

  private List<StudentDTO> getStudentListBasedOnCourseAndState(String state) throws UserDisplayableException {
    if (getSelectedCourse().isPresent()) {
      return STUDENT_DAO.getAllByStateAndCourse(state, getSelectedCourse().get().getNRC());
    }

    return List.of();
  }

  @Override
  public void loadDataListByActiveState() {
    loadStudentListBasedOnCourseSelectionAndState("ACTIVE");
  }

  @Override
  public void loadDataListByInactiveState() {
    loadStudentListBasedOnCourseSelectionAndState("RETIRED");
  }

  /**
   * Loads the student list based on the selected course and the provided state.
   * If the state is null, it loads all students for the selected course.
   *
   * @param nullableState The state to filter students by, or null to load all students for the selected course.
   */
  public void loadStudentListBasedOnCourseSelectionAndState(String nullableState) {
    try {
      if (nullableState != null) {
        tableStudent.setItems(
          FXCollections.observableList(getStudentListBasedOnCourseAndState(nullableState))
        );
      } else if (getSelectedCourse().isPresent()) {
        tableStudent.setItems(
          FXCollections.observableList(STUDENT_DAO.getAllByCourse(getSelectedCourse().get().getNRC()))
        );
      }
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  /**
   * Sets the action for the course selection combo box to load the student list based on the selected course
   * and the "ACTIVE" state.
   */
  public void setLoadStudentListOnCourseSelectionAction() {
    comboBoxCourse.setOnAction(e -> loadStudentListBasedOnCourseSelectionAndState("ACTIVE"));
  }

  public void handleOpenRegisterStudentModal() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Estudiante", "RegisterStudentModal", this::loadDataList)
    );
  }

  public void handleManageStudent() {
    StudentDTO selectedStudent = tableStudent.getSelectionModel().getSelectedItem();

    if (selectedStudent == null) return;

    ModalFacade.createAndDisplayContextModal(
      new ModalFacadeConfiguration("Gestinar Estudiante", "ManageStudentModal", this::loadDataList),
      selectedStudent
    );
  }

  public static void navigateToStudentListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Estudiantes", "ReviewStudentListPage");
  }
}
