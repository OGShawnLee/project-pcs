package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dao.EvaluationDAO;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

public class RegisterEvaluationController extends Controller{

    private final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();

    @FXML
    private TextField fieldIdProject;
    @FXML
    private TextField fieldIdStudent;
    @FXML
    private TextField fieldIdAcademic;
    @FXML
    private TextField fieldSkillGrade;
    @FXML
    private TextField fieldContentGrade;
    @FXML
    private TextField fieldWritingGrade;
    @FXML
    private TextField fieldRequirementsGrade;
    @FXML
    private TextField fieldFeedback;

    public void handleRegisterEvaluation(ActionEvent event) {
        try {
            // Validamos que los campos de texto sean correctos antes de continuar
            int idProject = Integer.parseInt(fieldIdProject.getText().trim());
            String idStudent = fieldIdStudent.getText().trim();
            String idAcademic = fieldIdAcademic.getText().trim();
            String skillGradeStr = fieldSkillGrade.getText().trim();
            String contentGradeStr = fieldContentGrade.getText().trim();
            String writingGradeStr = fieldWritingGrade.getText().trim();
            String requirementsGradeStr = fieldRequirementsGrade.getText().trim();
            String feedback = fieldFeedback.getText().trim();

            if (idStudent.isEmpty() || idAcademic.isEmpty()) {
                Modal.displayError("ID del estudiante y del académico son obligatorios.");
                return;
            }

            EvaluationDTO evaluation = new EvaluationDTO.EvaluationBuilder()
                    .setIDProject(idProject)
                    .setIDStudent(idStudent)
                    .setIDAcademic(idAcademic)
                    .setSkillGrade(skillGradeStr)
                    .setContentGrade(contentGradeStr)
                    .setWritingGrade(writingGradeStr)
                    .setRequirementsGrade(requirementsGradeStr)
                    .setFeedback(feedback)
                    .setCreatedAt(LocalDateTime.now())
                    .build();

            EVALUATION_DAO.createOne(evaluation);
            Modal.displaySuccess("Evaluación registrada correctamente.");
            returnToMainPage(event);

        } catch (NumberFormatException e) {
            Modal.displayError("Asegúrate de ingresar números válidos en los campos de calificación y proyecto.");
        } catch (SQLException e) {
            Modal.displayError("Error al registrar la evaluación en la base de datos.");
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void returnToMainPage(ActionEvent event) throws IOException {
        Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/EvaluatorMainPage.fxml")));
        Scene newScene = new Scene(newView);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }

    public static void navigateToRegisterEvaluationPage(Stage currentStage) {
        navigateTo(currentStage, "Registrar evaluación", "RegisterEvaluationPage");
    }
}
