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
import org.example.business.dao.filter.FilterPractice;
import org.example.business.dto.PracticeDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.StudentPracticeDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.util.ArrayList;
import java.util.List;

public class AssignProjectController extends ManageController<ProjectDTO> {
  private static final PracticeDAO PRACTICE_DAO = new PracticeDAO();
  private static final StudentDAO STUDENT_DAO = new StudentDAO();
  @FXML
  private Label labelProjectName;
  private List<StudentPracticeDTO> initialStudentPracticeList = new ArrayList<>();
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

  @Override
  public void initialize(ProjectDTO currentProject) {
    super.initialize(currentProject);
    labelProjectName.setText(currentProject.getName());
    initializeColumns();
    loadStudentLists();
    setAddStudentToSelectedListHandler();
    setRemoveStudentFromSelectedListHandler();
  }

  private void initializeColumns() {
    columnIDStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnNameStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastNameStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastNameStudentAvailable.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));

    columnIDStudentSelected.setCellValueFactory(new PropertyValueFactory<>("ID"));
    columnNameStudentSelected.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastNameStudentSelected.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastNameStudentSelected.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    tableStudentSelected.setItems(observableStudentSelectedList);
  }

  public void loadStudentLists() {
    observableStudentAvailableList.clear();
    observableStudentSelectedList.clear();

    try {
      List<StudentDTO> studentList = STUDENT_DAO.getAllWithNoProject();
      observableStudentAvailableList.addAll(studentList);
      tableStudentAvailable.setItems(observableStudentAvailableList);

      initialStudentPracticeList.clear();
      initialStudentPracticeList = new StudentPracticeDAO().getAllByProjectID(getContext().getID());

      for (StudentPracticeDTO studentPractice : initialStudentPracticeList) {
        observableStudentSelectedList.add(studentPractice.getStudentDTO());
      }
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar información de estudiantes.", e);
    }
  }

  public void handleAssign() throws UserDisplayableException {
    List<PracticeDTO> practiceDTOS = new ArrayList<>();
    List<String> assignedStudentNames = new ArrayList<>();

    for (StudentDTO student : observableStudentSelectedList) {
      boolean isAlreadyAssigned = initialStudentPracticeList.stream()
        .anyMatch(studentPractice -> studentPractice.getStudentDTO().getID().equals(student.getID()));

      if (isAlreadyAssigned) {
        continue;
      }

      practiceDTOS.add(
        new PracticeDTO.PracticeBuilder()
          .setIDStudent(student.getID())
          .setIDProject(getContext().getID())
          .setReasonOfAssignation(fieldReasonOfAssignation.getText())
          .build()
      );
      assignedStudentNames.add(student.getName());
    }

    PRACTICE_DAO.createMany(practiceDTOS);

    for (String assignedStudentName : assignedStudentNames) {
      AlertFacade.showSuccessAndWait("El proyecto ha asignado a " + assignedStudentName + " exitosamente.");
    }
  }

  public void handleUnassign() throws UserDisplayableException {
    List<FilterPractice> filterPractices = new ArrayList<>();
    List<String> unassignedStudentNames = new ArrayList<>();

    for (StudentPracticeDTO studentPractice : initialStudentPracticeList) {
      boolean isStillAssigned = observableStudentSelectedList.stream()
        .anyMatch(student -> studentPractice.getStudentDTO().getID().equals(student.getID()));

      if (isStillAssigned) {
        continue;
      }

      filterPractices.add(
        new FilterPractice(studentPractice.getStudentDTO().getID(), getContext().getID())
      );
      unassignedStudentNames.add(studentPractice.getStudentDTO().getName());
    }

    PRACTICE_DAO.deleteMany(filterPractices);

    for (String unassignedStudentName : unassignedStudentNames) {
      AlertFacade.showSuccessAndWait("El proyecto ha sido desasignado a " + unassignedStudentName + " exitosamente.");
    }
  }

  public void handleUpdateCurrentDataObject() {
    if (observableStudentSelectedList.size() > 3) {
      AlertFacade.showErrorAndWait("No se pueden asignar más de 3 estudiantes a un proyecto.");
      return;
    }

    try {
      handleAssign();
      handleUnassign();
      loadStudentLists();
    } catch (IllegalArgumentException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible guardar asignación del proyecto.", e);
    }
  }

  public void navigateToProjectList() {
    this.navigateFromThisPageTo("Lista de Proyectos", "ReviewProjectListPage");
  }

  public void setAddStudentToSelectedListHandler() {
    setRowDoubleClickHandler(
      tableStudentAvailable,
      student -> {
        if (student == null) {
          return null;
        }

        if (observableStudentSelectedList.size() >= getContext().getAvailablePlaces()) {
          AlertFacade.showErrorAndWait("No se pueden asignar más estudiantes al proyecto. Máximo: " + getContext().getAvailablePlaces());
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
        if (student == null) {
          return null;
        }

        observableStudentAvailableList.add(student);
        observableStudentSelectedList.remove(student);
        return null;
      }
    );
  }
}
