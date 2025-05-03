package org.example.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.business.dto.EvaluationDTO;
import org.example.db.dao.EnrollmentDAO;
import org.example.db.dao.EvaluationDAO;
import org.example.db.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReviewEvaluationsController {

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

    @FXML
    public void showTableEvaluations() {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIDStudent()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getFullName(cellData.getValue().getIDStudent())));
        nrcColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getNrc(cellData.getValue().getIDStudent())));

        visualizerColumn.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("Visualizar");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableRow() == null || getTableRow().getItem() == null ? null : createButtonForEvaluation(getTableRow().getItem()));
            }

            private Button createButtonForEvaluation(Object item) {
                EvaluationDTO evaluation = (EvaluationDTO) item;
                if (evaluation.getIDStudent() != null && !evaluation.getIDStudent().isBlank()) {
                    button.setOnAction(event -> openEvaluationDetailsPage(evaluation));
                    return button;
                }
                return null;
            }
        });

        loadEvaluations();
    }

    private String getFullName(String idStudent) {
        String fullName = "Desconocido";
        try {
            StudentDAO studentDAO = new StudentDAO();
            var student = studentDAO.getOne(idStudent);
            if (student != null) {
                fullName = student.getName() + " " + student.getPaternalLastName() + " " + student.getMaternalLastName();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullName;
    }

    private String getNrc(String idStudent) {
        String nrc = "No inscrito";
        try {
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            var enrollmentList = enrollmentDAO.getAll();
            for (var enrollment : enrollmentList) {
                if (enrollment.getIDStudent().equals(idStudent)) {
                    nrc = enrollment.getIDCourse();
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrc;
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
            e.printStackTrace();
            AlertDialog.showError("No se pudo cargar la vista de detalles.");
        }
    }

    private void loadEvaluations() {
        try {
            List<EvaluationDTO> evaluationList = evaluationDAO.getAll();
            ObservableList<EvaluationDTO> observableList = FXCollections.observableArrayList(evaluationList);
            evaluationTable.setItems(observableList);
        } catch (SQLException e) {
            AlertDialog.showError("No se pudo mostrar los datos debido a un error en el sistema");
        }
    }


    public void updateTable() {
        try {
            List<EvaluationDTO> evaluationList = evaluationDAO.getAll();
            ObservableList<EvaluationDTO> observableList = FXCollections.observableArrayList(evaluationList);
            evaluationTable.setItems(observableList);
        } catch (SQLException e) {
            AlertDialog.showError("No se pudo actualizar la tabla debido a un error en el sistema.");
        }
    }

    @FXML
    public void initialize() {
        showTableEvaluations();
    }
}
