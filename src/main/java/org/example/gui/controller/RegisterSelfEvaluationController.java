package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.example.business.dao.SelfEvaluationDAO;
import org.example.business.dto.SelfEvaluationDTO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class RegisterSelfEvaluationController extends Controller {
    private final SelfEvaluationDAO SELF_EVALUATION_DAO = new SelfEvaluationDAO();

    @FXML private ComboBox<Integer> followUpGrade;
    @FXML private ComboBox<Integer> safetyGrade;
    @FXML private ComboBox<Integer> knowledgeApplicationGrade;
    @FXML private ComboBox<Integer> interestingGrade;
    @FXML private ComboBox<Integer> productivityGrade;
    @FXML private ComboBox<Integer> congruentGrade;
    @FXML private ComboBox<Integer> informedByOrganization;
    @FXML private ComboBox<Integer> regulatedByOrganization;
    @FXML private ComboBox<Integer> importanceForProfessionalDevelopment;

    @FXML
    public void initialize() {
        loadGradeCombos();
    }

    private void loadGradeCombos() {
        ObservableList<Integer> grades = FXCollections.observableArrayList();
        for (int i = 1; i <= 10; i++) {
            grades.add(i);
        }

        followUpGrade.setItems(grades);
        safetyGrade.setItems(grades);
        knowledgeApplicationGrade.setItems(grades);
        interestingGrade.setItems(grades);
        productivityGrade.setItems(grades);
        congruentGrade.setItems(grades);
        informedByOrganization.setItems(grades);
        regulatedByOrganization.setItems(grades);
        importanceForProfessionalDevelopment.setItems(grades);
    }

    public void handleRegister() {
        if (!(Session.getCurrentUser() instanceof StudentDTO student)) {
            Modal.displayError("Solo un estudiante puede registrar su autoevaluación.");
            return;
        }

        try {
            if (SELF_EVALUATION_DAO.getOne(student.getID()) != null) {
                Modal.displayError("Ya existe una autoevaluación registrada para este estudiante.");
                return;
            }

            SelfEvaluationDTO evaluationDTO = new SelfEvaluationDTO.SelfEvaluationBuilder()
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

            SELF_EVALUATION_DAO.createOne(evaluationDTO);
            Modal.displaySuccess("La autoevaluación ha sido registrada exitosamente.");

        } catch (IllegalArgumentException e) {
            Modal.displayError("Campo obligatorio no seleccionado: " + e.getMessage());
        } catch (SQLException e) {
            Modal.displayError("No fue posible registrar la autoevaluación debido a un error en el sistema.");
        }
    }

}
