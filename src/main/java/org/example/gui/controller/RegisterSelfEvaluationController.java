package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.business.dao.SelfEvaluationDAO;
import org.example.business.dto.SelfEvaluationDTO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;
import org.example.gui.Session;

import java.sql.SQLException;
import java.util.stream.IntStream;

public class RegisterSelfEvaluationController extends Controller{
    @FXML
    private ComboBox<Integer> followUpGrade;
    @FXML
    private ComboBox<Integer> safetyGrade;
    @FXML
    private ComboBox<Integer> knowledgeApplicationGrade;
    @FXML
    private ComboBox<Integer> interestingGrade;
    @FXML
    private ComboBox<Integer> productivityGrade;
    @FXML
    private ComboBox<Integer> congruentGrade;
    @FXML
    private ComboBox<Integer> informedByOrganization;
    @FXML
    private ComboBox<Integer> regulatedByOrganization;
    @FXML
    private ComboBox<Integer> importanceForProfessionalDevelopment;

    SelfEvaluationDAO SELF_EVALUATION_DAO;

    @FXML
    public void initialize() {
        SELF_EVALUATION_DAO = new SelfEvaluationDAO();
        var numbers = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 10).boxed().toList()
        );

        followUpGrade.setItems(numbers);
        safetyGrade.setItems(numbers);
        knowledgeApplicationGrade.setItems(numbers);
        interestingGrade.setItems(numbers);
        productivityGrade.setItems(numbers);
        congruentGrade.setItems(numbers);
        informedByOrganization.setItems(numbers);
        regulatedByOrganization.setItems(numbers);
        importanceForProfessionalDevelopment.setItems(numbers);
    }

    public void handleRegisterSelfEvaluation() {
        Object user = Session.getCurrentUser();
        if (!(user instanceof StudentDTO student)) {
            Modal.displayError("Solo un estudiante puede registrar su autoevaluación.");
            return;
        }

        try {
            SelfEvaluationDTO dataObjectSelfEvaluation = new SelfEvaluationDTO.SelfEvaluationBuilder()
                    .setIDStudent(student.getID())
                    .setFollowUpGrade(followUpGrade.getValue())
                    .setSafetyGrade(safetyGrade.getValue())
                    .setKnowledgeApplicationGrade(knowledgeApplicationGrade.getValue())
                    .setInterestingGrade(interestingGrade.getValue())
                    .setProductivityGrade(productivityGrade.getValue())
                    .setCongruentGrade(congruentGrade.getValue())
                    .setInformedByOrganization(informedByOrganization.getValue())
                    .setRegulatedByOrganization(regulatedByOrganization.getValue())
                    .setImportanceForProfessionalDevelopment(importanceForProfessionalDevelopment.getValue())
                    .build();

            SelfEvaluationDTO dataObjectExistingSelfEvaluation = SELF_EVALUATION_DAO.getOne(dataObjectSelfEvaluation.getIDStudent());

            if (dataObjectExistingSelfEvaluation != null) {
                Modal.displayError(
                        "No ha sido posible registrar la autoevaluacion debido a que ya existe una organización registrada con ese correo electrónico."
                );
                return;
            }

            SELF_EVALUATION_DAO.createOne(dataObjectSelfEvaluation);
            Modal.displaySuccess("La autoevaluacion ha sido registrada exitosamente.");
            StudentMainController.navigateToStudentMain(getScene());
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible registrar la organización debido a un error de sistema.");
        }
    }

    public void goBack() {
        StudentMainController.navigateToStudentMain(getScene());
    }

    public static void navigateToRegisterSelfEvaluation(Stage currentStage) {
        navigateTo(currentStage, "Registro de autoevaluacion", "RegisterSelfEvaluationPage");
    }
}
