package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.business.dto.CourseDTO;
import org.example.business.dao.CourseDAO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ReviewCourseListController extends ReviewListController implements FilterableByStateController {
  private static final CourseDAO COURSE_DAO = new CourseDAO();
  @FXML
  private TableView<CourseDTO> tableAcademic;
  @FXML
  private TableColumn<CourseDTO, String> columnNRC;
  @FXML
  private TableColumn<CourseDTO, String> columnAcademic;
  @FXML
  private TableColumn<CourseDTO, String> columnSemester;
  @FXML
  private TableColumn<CourseDTO, CourseDTO.Section> columnSection;
  @FXML
  private TableColumn<CourseDTO, CourseDTO.State> columnState;
  @FXML
  private TableColumn<CourseDTO, String> columnCreatedAt;

  @Override
  public void loadTableColumns() {
    columnNRC.setCellValueFactory(new PropertyValueFactory<>("NRC"));
    columnAcademic.setCellValueFactory(new PropertyValueFactory<>("fullNameAcademic"));
    columnSemester.setCellValueFactory(new PropertyValueFactory<>("formattedSemester"));
    columnSection.setCellValueFactory(new PropertyValueFactory<>("section"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
  }

  @Override
  public void loadDataList() {
    try {
      tableAcademic.setItems(
        FXCollections.observableList(
          COURSE_DAO.getAll()
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de cursos debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByActiveState() {
    try {
      tableAcademic.setItems(
        FXCollections.observableList(
          COURSE_DAO.getAllByState(CourseDTO.State.ON_GOING)
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de cursos activos debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByInactiveState() {
    try {
      tableAcademic.setItems(
        FXCollections.observableList(
          COURSE_DAO.getAllByState(CourseDTO.State.COMPLETED)
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de cursos inactivos debido a un error de sistema."
      );
    }
  }

  public void handleOpenRegisterCourseModal() {
    Modal.display(
      "Registrar Académico",
      "RegisterAcademicModal",
      this::loadDataList
    );
  }

  public void handleManageCourse() {
    CourseDTO selectedCourse = tableAcademic.getSelectionModel().getSelectedItem();

    if (selectedCourse == null) return;

    Modal.displayManageModal(
      "Gestionar Curso",
      "ManageCourseModal",
      this::loadDataList,
      selectedCourse
    );
  }

  public static void navigateToCourseListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Cursos", "ReviewCourseListPage");
  }
}
