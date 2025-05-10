package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.business.dao.PracticeDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dao.StudentPracticeDAO;
import org.example.business.dto.PracticeDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.StudentPracticeDTO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class AssignProjectController extends ManageController<ProjectDTO> {
  private static final PracticeDAO PRACTICE_DAO = new PracticeDAO();
  private static final StudentDAO STUDENT_DAO = new StudentDAO();
  @FXML
  private Label labelProjectName;
  private List<StudentPracticeDTO> initialStudentPracticeList;
  @FXML
  private TableView<StudentDTO> tableStudentAvailable;
  @FXML
  private TableColumn<StudentDTO, String> columnIDStudentAvailable;
  @FXML
  private TableColumn<StudentDTO, String> columnNameStudentAvailable;
  @FXML
  private TableColumn<StudentDTO, String> columnPaternalLastNameStudentAvailable;
  @FXML
  private TableColumn<StudentDTO, String> columnMaternalLastNameStudentAvailable;
  private final ObservableList<StudentDTO> observableStudentAvailableList = FXCollections.observableArrayList();
  @FXML
  private TableView<StudentDTO> tableStudentSelected;
  @FXML
  private TableColumn<StudentDTO, String> columnIDStudentSelected;
  @FXML
  private TableColumn<StudentDTO, String> columnNameStudentSelected;
  @FXML
  private TableColumn<StudentDTO, String> columnPaternalLastNameStudentSelected;
  @FXML
  private TableColumn<StudentDTO, String> columnMaternalLastNameStudentSelected;
  @FXML
  private TextArea fieldReasonOfAssignation;
  private final ObservableList<StudentDTO> observableStudentSelectedList = FXCollections.observableArrayList();

  public void setAddStudentToSelectedListHandler() {
    setRowDoubleClickHandler(
      tableStudentAvailable,
      student -> {
        if (student == null) {
          return null;
        }

        observableStudentSelectedList.add(student);
        observableStudentAvailableList.remove(student);
        return null;
      }
    );
  }

  public void setRemoveStudentFromSelectedListHandler() {
    setRowDoubleClickHandler(
      tableStudentSelected,
      student -> {
        if (student == null || isAlreadyAssigned(student)) {
          return null;
        }

        observableStudentAvailableList.add(student);
        observableStudentSelectedList.remove(student);
        return null;
      }
    );
  }

  private boolean isAlreadyAssigned(StudentDTO student) {
    return initialStudentPracticeList.stream().anyMatch(it -> it.getStudentDTO().getID().equals(student.getID()));
  }

  @Override
  public void initialize(ProjectDTO currentProject) {
    super.initialize(currentProject);
    labelProjectName.setText(currentProject.getName());
    loadAvailableStudentList();
    setAddStudentToSelectedListHandler();
    setRemoveStudentFromSelectedListHandler();
  }

  private void loadAvailableStudentList() {
    columnIDStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnNameStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastNameStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastNameStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));

    columnIDStudentSelected.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnNameStudentSelected.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastNameStudentSelected.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastNameStudentSelected.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    tableStudentSelected.setItems(observableStudentSelectedList);

    try {
      List<StudentDTO> studentList = STUDENT_DAO.getAllWithNoProject();
      observableStudentAvailableList.addAll(studentList);
      tableStudentAvailable.setItems(observableStudentAvailableList);

      initialStudentPracticeList = StudentPracticeDAO.getAllByProjectID(getCurrentDataObject().getID());

      for (StudentPracticeDTO studentPractice : initialStudentPracticeList) {
        observableStudentSelectedList.add(studentPractice.getStudentDTO());
      }
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de estudiantes debido a un error de sistema." + e.getMessage()
      );
    }
  }

  public void handleUpdateCurrentDataObject() {
    if (observableStudentSelectedList.isEmpty()) {
      Modal.displayError("No se ha seleccionado ningún estudiante.");
      return;
    }

    if (observableStudentSelectedList.size() > 3) {
      Modal.displayError("No se pueden asignar más de 3 estudiantes a un proyecto.");
      return;
    }

    try {
      for (StudentDTO student : observableStudentSelectedList) {
        if (isAlreadyAssigned(student)) {
          continue;
        }

        PRACTICE_DAO.createOne(
          new PracticeDTO.PracticeBuilder()
            .setIDStudent(student.getID())
            .setIDProject(getCurrentDataObject().getID())
            .setReasonOfAssignation(fieldReasonOfAssignation.getText())
            .build()
        );

        Modal.displaySuccess("El proyecto ha sido asignado con exito.");
      }
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    }  catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible asignar el proyecto debido a un error de sistema." + e.getMessage()
      );
    }
  }

  public void navigateToProjectList() {
    this.navigateFromThisPageTo("Lista de Proyectos", "ReviewProjectListPage");
  }
}
