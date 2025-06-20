package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.example.business.auth.AuthClient;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.PracticeDAO;
import org.example.business.dao.filter.FilterEvaluation;
import org.example.business.dto.*;
import org.example.business.dao.EvaluationDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class RegisterEvaluationController extends Controller{

    private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
    private static final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();
    private static final PracticeDAO PRACTICE_DTO = new PracticeDAO();

    @FXML
    private ComboBox<PracticeDTO> comboBoxPractice;
    @FXML
    private ComboBox<Integer> comboBoxSkillGrade;
    @FXML
    private ComboBox<Integer> comboBoxContentGrade;
    @FXML
    private ComboBox<Integer> comboBoxWritingGrade;
    @FXML
    private ComboBox<Integer> comboBoxRequirementsGrade;
    @FXML
    private TextArea textAreaFeedback;

    public void initialize() {
        loadComboBoxEnrollment();
        initializeGradeCombos();
    }

    private void initializeGradeCombos() {
        ObservableList<Integer> grades = FXCollections.observableArrayList();
        for (int i = 1; i <= 10; i++) {
            grades.add(i);
        }

        comboBoxSkillGrade.setItems(grades);
        comboBoxContentGrade.setItems(grades);
        comboBoxWritingGrade.setItems(grades);
        comboBoxRequirementsGrade.setItems(grades);
    }


    public void loadComboBoxEnrollment() {
        try {
            List<PracticeDTO> enrollmentList = PRACTICE_DTO.getAll();

            if (enrollmentList.isEmpty()) {
                Modal.displayError("No existe una organización. Por favor, registre una organización antes de registrar un proyecto.");
                return;
            }

            comboBoxPractice.getItems().addAll(enrollmentList);
            comboBoxPractice.setValue(enrollmentList.getFirst());
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
        }
    }

    public void handleRegister() {
        try {
            AcademicDTO currentAcademic = ACADEMIC_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());
            EvaluationDTO evaluationDTO = new EvaluationDTO.EvaluationBuilder()
                    .setIDProject(comboBoxPractice.getValue().getIDProject())
                    .setIDStudent(comboBoxPractice.getValue().getIDStudent())
                    .setIDAcademic(currentAcademic.getID())
                    .setContentCongruenceGrade(String.valueOf(comboBoxContentGrade.getValue()))
                    .setMethodologicalRigorGrade(String.valueOf(comboBoxRequirementsGrade.getValue()))
                    .setWritingGrade(String.valueOf(comboBoxWritingGrade.getValue()))
                    .setAdequateUseGrade(String.valueOf(comboBoxSkillGrade))
                    .setFeedback(textAreaFeedback.getText())
                    .build();

            FilterEvaluation filterEvaluation = new FilterEvaluation(
                    comboBoxPractice.getValue().getIDProject(),
                    comboBoxPractice.getValue().getIDStudent(),
                    currentAcademic.getID()
            );

            EvaluationDTO existingEvaluationDTO = EVALUATION_DAO.getOne(filterEvaluation);
            if (existingEvaluationDTO != null) {
                Modal.displayError("No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador.");
                return;
            }

            EVALUATION_DAO.createOne(evaluationDTO);
            Modal.displaySuccess("El académico ha sido registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Modal.displayError("No ha sido posible registrar académico debido a un error en el sistema.");
        }
    }

}
