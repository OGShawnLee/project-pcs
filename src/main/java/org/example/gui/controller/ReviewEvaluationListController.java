package org.example.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.business.dao.*;
import org.example.business.dao.filter.FilterEvaluation;
import org.example.business.dto.*;
import org.example.gui.Modal;
import org.example.gui.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ReviewEvaluationListController extends Controller {

    @FXML
    private TableView<EvaluationDTO> evaluationTable;
    @FXML
    private TableColumn<EvaluationDTO, String> idColumn;
    @FXML
    private TableColumn<EvaluationDTO, Button> visualizerColumn;
    @FXML
    private TableColumn<EvaluationDTO, String> nameColumn;
    @FXML
    private TableColumn<EvaluationDTO, String> nrcColumn;

    private final EvaluationDAO evaluationDAO = new EvaluationDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final PracticeDAO practiceDAO = new PracticeDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final AcademicDAO academicDAO = new AcademicDAO();

    private final Map<String, String> studentFullNames = new HashMap<>();
    private final Map<String, String> studentNrcs = new HashMap<>();

    private String loggedUserId;
    private boolean isStudent;
    private String userType = "desconocido";

    @FXML
    public void initialize() {
        identifyUserFromSession();
        loadStudentNames();
        loadStudentNRC();
        setupTableColumns();
        loadEvaluations();
    }

    private void identifyUserFromSession() {
        Object user = Session.getCurrentUser();
        switch (user) {
            case null -> Modal.displayError("No hay un usuario autenticado");
            case StudentDTO student -> {
                loggedUserId = student.getID();
                isStudent = true;
                userType = "estudiante";
            }
            case AcademicDTO academic -> {
                loggedUserId = academic.getID();
                isStudent = false;
                userType = switch (academic.getRole()) {
                    case "PROFESSOR" -> "profesor";
                    case "EVALUATOR" -> "evaluador";
                    default -> "desconocido";
                };
            }
            default -> Modal.displayError("Usuario desconocido");
        }
    }

    private void loadStudentNames() {
        try {
            List<StudentDTO> students = studentDAO.getAll();
            for (StudentDTO student : students) {
                String fullName = student.getName() + " " + student.getPaternalLastName() + " " + student.getMaternalLastName();
                studentFullNames.put(student.getID(), fullName);
            }
        } catch (SQLException e) {
            Modal.displayError("Error cargando nombres de estudiantes");
        }
    }

    private void loadStudentNRC() {
        try {
            List<EnrollmentDTO> enrollments = enrollmentDAO.getAll();
            for (EnrollmentDTO enrollment : enrollments) {
                studentNrcs.put(enrollment.getIDStudent(), enrollment.getIDCourse());
            }
        } catch (SQLException e) {
            Modal.displayError("Error cargando NRCs de estudiantes");
        }
    }

    private String getFullName(String idStudent) {
        return studentFullNames.getOrDefault(idStudent, "Desconocido");
    }

    private String getNrc(String idStudent) {
        return studentNrcs.getOrDefault(idStudent, "No inscrito");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIDStudent()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getFullName(cellData.getValue().getIDStudent())));
        nrcColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getNrc(cellData.getValue().getIDStudent())));

        visualizerColumn.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("Visualizar");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    EvaluationDTO evaluation = getTableRow().getItem();
                    button.setOnAction(event -> openEvaluationDetailsPage(evaluation));
                    setGraphic(button);
                }
            }
        });
    }

    private void loadEvaluations() {
        if (isStudent) {
            loadEvaluationsForStudent();
        } else {
            loadEvaluationsForAcademic();
        }
    }

    private void loadEvaluationsForStudent() {
        try {
            StudentDTO student = studentDAO.getOne(loggedUserId);
            PracticeDTO practice = practiceDAO.getAll().stream()
                    .filter(p -> p.getIDStudent().equals(student.getID()))
                    .findFirst()
                    .orElse(null);

            if (practice == null) {
                Modal.displayError("No se encontró una práctica asignada.");
                return;
            }

            EnrollmentDTO enrollment = enrollmentDAO.getAll().stream()
                    .filter(e -> e.getIDStudent().equals(student.getID()))
                    .findFirst()
                    .orElse(null);

            if (enrollment == null) {
                Modal.displayError("No se encontró una inscripción del curso.");
                return;
            }

            ProjectDTO project = projectDAO.getOne(practice.getIDProject());
            CourseDTO course = courseDAO.getOne(enrollment.getIDCourse());

            FilterEvaluation filter = new FilterEvaluation(project.getID(), loggedUserId, course.getIDAcademic());
            EvaluationDTO evaluation = evaluationDAO.getOne(filter);

            if (evaluation != null) {
                evaluationTable.setItems(FXCollections.observableArrayList(evaluation));
            }

        } catch (SQLException e) {
            Modal.displayError("No se pudo cargar evaluaciones del estudiante.");
        }
    }

    private void loadEvaluationsForAcademic() {
        try {
            AcademicDTO academic = academicDAO.getOne(loggedUserId);

            List<CourseDTO> courses = courseDAO.getAll().stream()
                    .filter(course -> course.getIDAcademic().equals(academic.getID()))
                    .toList();

            List<EnrollmentDTO> enrollments = enrollmentDAO.getAll().stream()
                    .filter(enrollment -> courses.stream()
                            .anyMatch(course -> course.getNRC().equals(enrollment.getIDCourse())))
                    .toList();

            List<EvaluationDTO> evaluations = new ArrayList<>();

            for (EnrollmentDTO enrollment : enrollments) {
                String studentId = enrollment.getIDStudent();

                PracticeDTO practice = practiceDAO.getAll().stream()
                        .filter(p -> p.getIDStudent().equals(studentId))
                        .findFirst()
                        .orElse(null);

                if (practice == null) continue;

                ProjectDTO project = projectDAO.getOne(practice.getIDProject());
                if (project == null) continue;

                FilterEvaluation filter = new FilterEvaluation(project.getID(), studentId, academic.getID());
                EvaluationDTO evaluation = evaluationDAO.getOne(filter);

                if (evaluation != null) {
                    evaluations.add(evaluation);
                }
            }

            evaluationTable.setItems(FXCollections.observableArrayList(evaluations));
        } catch (SQLException e) {
            Modal.displayError("No se pudo cargar evaluaciones del académico.");
        }
    }

    private void openEvaluationDetailsPage(EvaluationDTO evaluation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/VisualizeEvaluationPage.fxml"));
            Parent root = loader.load();

            VisualizeEvaluationController controller = loader.getController();
            controller.setEvaluation(evaluation);

            Stage stage = new Stage();
            stage.setTitle("Detalles de la Evaluación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Modal.displayError("No se pudo cargar la vista de detalles.");
        }
    }

    public void navigateToMain() {
        identifyUserFromSession();
        switch (userType) {
            case "estudiante" -> StudentMainController.navigateToStudentMain(getScene());
            case "profesor" -> AcademicMainController.navigateToAcademicMain(getScene());
            case "evaluador" -> EvaluatorMainController.navigateToEvaluatorMain(getScene());
            default -> Modal.displayError("Tipo de usuario no reconocido.");
        }
    }

    public static void navigateToEvaluationListPage(Stage currentStage) {
        navigateTo(currentStage, "Lista de evaluaciones", "ReviewEvaluationListPage");
    }
}
