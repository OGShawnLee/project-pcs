package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.example.business.auth.AuthClient;
import org.example.business.dao.*;
import org.example.business.dao.filter.FilterEvaluation;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dto.PracticeDTO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class RegisterEvaluationController extends Controller {

    private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
    private static final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();
    private static final PracticeDAO PRACTICE_DAO = new PracticeDAO();
    private static final ConfigurationDAO CONFIGURATION_DAO = new ConfigurationDAO();

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
            List<PracticeDTO> practiceList = PRACTICE_DAO.getAll();

            if (practiceList.isEmpty()) {
                Modal.displayError("No existe una práctica. Por favor, registre una práctica antes de registrar una evaluación.");
                return;
            }

            comboBoxPractice.getItems().addAll(practiceList);
            comboBoxPractice.setValue(practiceList.getFirst());
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible cargar las prácticas debido a un error en el sistema.");
        }
    }

    public void handleRegister() {
        try {
            if (comboBoxPractice.getValue() == null || comboBoxSkillGrade.getValue() == null ||
                    comboBoxContentGrade.getValue() == null || comboBoxWritingGrade.getValue() == null ||
                    comboBoxRequirementsGrade.getValue() == null || textAreaFeedback.getText().isBlank()) {
                Modal.displayError("Todos los campos deben estar completos.");
                return;
            }

            AcademicDTO currentAcademic = ACADEMIC_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());

            List<ConfigurationDTO> currentConfiguration = CONFIGURATION_DAO.getAll();
            EvaluationDTO.Kind period = null;

            for (ConfigurationDTO config : currentConfiguration) {
                String configName = String.valueOf(config.name());
                if (configName.equals(Configuration.EVALUATION_ENABLED_FIRST.name()) && config.isEnabled()) {
                    period = EvaluationDTO.Kind.FIRST_PERIOD;
                    break;
                } else if (configName.equals(Configuration.EVALUATION_ENABLED_SECOND.name()) && config.isEnabled()) {
                    period = EvaluationDTO.Kind.SECOND_PERIOD;
                    break;
                } else if (configName.equals(Configuration.EVALUATION_ENABLED_FINAL.name()) && config.isEnabled()) {
                    period = EvaluationDTO.Kind.FINAL;
                    break;
                }
            }


            EvaluationDTO evaluationDTO = new EvaluationDTO.EvaluationBuilder()
                    .setIDProject(comboBoxPractice.getValue().getIDProject())
                    .setIDStudent(comboBoxPractice.getValue().getIDStudent())
                    .setIDAcademic(currentAcademic.getID())
                    .setContentCongruenceGrade(String.valueOf(comboBoxContentGrade.getValue()))
                    .setMethodologicalRigorGrade(String.valueOf(comboBoxRequirementsGrade.getValue()))
                    .setWritingGrade(String.valueOf(comboBoxWritingGrade.getValue()))
                    .setAdequateUseGrade(String.valueOf(comboBoxSkillGrade.getValue()))
                    .setFeedback(textAreaFeedback.getText())
                    .setKind(period)
                    .build();

            FilterEvaluation filterEvaluation = new FilterEvaluation(
                    comboBoxPractice.getValue().getIDProject(),
                    comboBoxPractice.getValue().getIDStudent(),
                    currentAcademic.getID()
            );

            EvaluationDTO existingEvaluationDTO = EVALUATION_DAO.getOne(filterEvaluation);
            if (existingEvaluationDTO != null) {
                Modal.displayError("Ya existe una evaluación registrada para esta práctica.");
                return;
            }

            EVALUATION_DAO.createOne(evaluationDTO);
            Modal.displaySuccess("La evaluación ha sido registrada exitosamente.");
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Modal.displayError("No ha sido posible registrar la evaluación debido a un error en el sistema.");
        }
    }
}
