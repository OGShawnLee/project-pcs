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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.business.dto.StudentDTO;
import org.example.db.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ReviewStudentsController {

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
            AlertDialog.showError("No se pudo mostrar los datos debido a un error en el sistema");
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
            AlertDialog.showError("Error al filtrar por estado");
        }
    }

    public void updateTable() {
        try {
            List<StudentDTO> studentList = studentDAO.getAll();
            ObservableList<StudentDTO> observableList = FXCollections.observableArrayList(studentList);
            studentTable.setItems(observableList);
        } catch (SQLException e) {
            AlertDialog.showError("No se pudo actualizar la tabla debido a un error en el sistema.");
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
    public void openManageStudent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/InsertId.fxml"));

        InsertToManageController controller = new InsertToManageController();
        Stage reviewStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        controller.setReviewStage(reviewStage);
        loader.setController(controller);

        Parent newView = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(newView));
        stage.setTitle("");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(Event::consume);
        controller.setStage(stage);
        stage.show();
    }

    @FXML
    public void openFinalGradeStudent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/InsertId.fxml"));

        InsertToFinalGradeController controller = new InsertToFinalGradeController();
        Stage reviewStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        controller.setReviewStage(reviewStage);
        loader.setController(controller);

        Parent newView = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(newView));
        stage.setTitle("");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(Event::consume);
        controller.setStage(stage);
        stage.show();
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
}
