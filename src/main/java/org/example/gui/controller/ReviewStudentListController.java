package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReviewStudentListController extends Controller {

    @FXML
    private ComboBox<String> stateView;
    @FXML
    private TableView<StudentDTO> studentTable;
    @FXML
    private TableColumn<StudentDTO, String> idColumn;
    @FXML
    private TableColumn<StudentDTO, String> paternalLastNameColumn;
    @FXML
    private TableColumn<StudentDTO, String> maternalLastNameColumn;
    @FXML
    private TableColumn<StudentDTO, String> nameColumn;
    @FXML
    private TableColumn<StudentDTO, String> emailColumn;
    @FXML
    private TableColumn<StudentDTO, String> createdAtColumn;
    @FXML
    private TableColumn<StudentDTO, String> finalGradeColumn;
    @FXML
    private TableColumn<StudentDTO, String> stateColumn;

    private final StudentDAO studentDAO = new StudentDAO();

    public void initialize() {
        stateView.getItems().addAll("Activo", "Archivado", "Todos");
        stateView.setOnAction(event -> {
            String option = stateView.getValue();
            if (option != null) {
                switch (option) {
                    case "Todos" -> showTableStudents();
                    case "Activo" -> showStudentsByState("ACTIVE");
                    case "Archivado" -> showStudentsByState("RETIRED");
                }
            }
        });
        stateView.setValue("Todos");
        showTableStudents();
    }

    public void showTableStudents() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        paternalLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
        maternalLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        finalGradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        try {
            List<StudentDTO> studentList = studentDAO.getAll();
            ObservableList<StudentDTO> observableList = FXCollections.observableArrayList(studentList);
            studentTable.setItems(observableList);
        } catch (SQLException e) {
            Modal.displayError("No se pudo mostrar los datos debido a un error en el sistema");
        }
    }

    public void showStudentsByState(String state) {
        try {
            List<StudentDTO> studentList = studentDAO.getAll();
            List<StudentDTO> filteredList = studentList.stream()
                    .filter(student -> student.getState().equalsIgnoreCase(state))
                    .toList();
            ObservableList<StudentDTO> observableList = FXCollections.observableArrayList(filteredList);
            studentTable.setItems(observableList);
        } catch (SQLException e) {
            Modal.displayError("Error al filtrar por estado debido a la conexion de la base de datos");
        }
    }

    public void openRegisterStudent() {
        RegisterStudentController.navigateToRegisterStudentPage(getScene());
    }

    public void openManageStudent() {
        Optional<String> result = Modal.promptText(
                "Buscar estudiante",
                "Ingrese la matrícula del estudiante",
                "Matrícula:"
        );

        result.ifPresent(idStudent -> {
            try {
                StudentDTO student = studentDAO.getOne(idStudent);

                if (student == null) {
                    Modal.displayError("Estudiante no encontrado");
                    return;
                }

                Controller.navigateToManagePage(
                        getScene(),
                        "Gestión de estudiante",
                        "ManageStudentPage",
                        student
                );
            } catch (SQLException e) {
                Modal.displayError("Error al buscar el estudiante en la base de datos.");
            }
        });
    }

    public void goBackToMenu() {
        AcademicMainController.navigateToAcademicMain(getScene());
    }

    public void openFinalGradeStudent() {
        Optional<String> result = Modal.promptText(
                "Buscar estudiante",
                "Ingrese la matrícula del estudiante",
                "Matrícula:"
        );

        result.ifPresent(idStudent -> {
            try {
                StudentDTO student = studentDAO.getOne(idStudent);

                if (student == null) {
                    Modal.displayError("Estudiante no encontrado");
                    return;
                }

                Controller.navigateToManagePage(
                        getScene(),
                        "Gestión de estudiante",
                        "RegisterFinalGradePage",
                        student
                );
            } catch (SQLException e) {
                Modal.displayError("Error al buscar el estudiante en la base de datos.");
            }
        });
        }

    public static void navigateToStudentListPage(Stage currentStage) {
        navigateTo(currentStage, "Listado de Estudiantes", "ReviewStudentListPage");
    }
}
