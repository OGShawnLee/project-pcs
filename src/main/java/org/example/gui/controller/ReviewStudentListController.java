package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReviewStudentListController {

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
    @FXML
    private Button registerStudentButton;

    private final StudentDAO studentDAO = new StudentDAO();

    @FXML
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

    @FXML
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

    public void updateTable() {
        try {
            List<StudentDTO> studentList = studentDAO.getAll();
            ObservableList<StudentDTO> observableList = FXCollections.observableArrayList(studentList);
            studentTable.setItems(observableList);
        } catch (SQLException e) {
            Modal.displayError("No se pudo actualizar la tabla debido a un error en el sistema.");
        }
    }

    @FXML
    public void openRegisterStudent(ActionEvent actionEvent) throws IOException {
        Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/RegisterStudentPage.fxml")));
        Scene newScene = new Scene(newView);

        Stage stage = (Stage) registerStudentButton.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }

    @FXML
    public void openManageStudent(ActionEvent event) {
        Optional<String> result = Modal.promptText(
                "Buscar estudiante",
                "Ingrese la matrícula del estudiante",
                "Matrícula:"
        );

        result.ifPresent(idStudent -> {
            try {
                StudentDAO dao = new StudentDAO();
                StudentDTO student = dao.getOne(idStudent);

                if (student == null) {
                    Modal.displayError("Estudiante no encontrado");

                    changeScene("/org/example/ReviewStudentListPage.fxml", event);
                    return;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ManageStudentPage.fxml"));
                Parent manageView = loader.load();

                ManageStudentController controller = loader.getController();
                controller.setStudent(student);
                changeScene(manageView, event);
            } catch (SQLException e) {
                Modal.displayError("Error al buscar el estudiante en la base de datos.");
            } catch (IOException e) {
                Modal.displayError("Error al cargar la vista de gestión del estudiante.");
            }
        });
    }




    @FXML
    public void openFinalGradeStudent(ActionEvent event) {
        Optional<String> result = Modal.promptText(
                "Buscar estudiante",
                "Ingrese la matrícula del estudiante",
                "Matrícula:"
        );

        result.ifPresent(idStudent -> {
            try {
                StudentDAO dao = new StudentDAO();
                StudentDTO student = dao.getOne(idStudent);

                if (student == null) {
                    Modal.displayError("Estudiante no encontrado");
                    changeScene("/org/example/ReviewStudentListPage.fxml", event);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/FinalGradeStudentPage.fxml"));
                Parent gradeView = loader.load();

                RegisterFinalGradeController controller = loader.getController();
                controller.setStudent(student);

                changeScene(gradeView, event);
            } catch (SQLException e) {
                Modal.displayError("Error al buscar el estudiante en la base de datos.");
            } catch (IOException e) {
                Modal.displayError("Error al cargar la vista de calificaciones finales.");
            }
        });
    }


    @FXML
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

    private void changeScene(Object source, ActionEvent event) throws IOException {
        Parent view;

        if (source instanceof String path) {
            view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        } else if (source instanceof Parent parent) {
            view = parent;
        } else {
            throw new IllegalArgumentException("Tipo de argumento no válido para cambiar de escena.");
        }

        Scene newScene = new Scene(view);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }

}
