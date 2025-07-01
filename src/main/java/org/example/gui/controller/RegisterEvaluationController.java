package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.example.business.auth.AuthClient;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dao.EvaluationDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dao.ProjectDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dao.filter.FilterEvaluation;
import org.example.business.dto.enumeration.ConfigurationName;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class RegisterEvaluationController extends Controller {
  private static final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();
  private static final StudentDAO STUDENT_DAO = new StudentDAO();
  private static final ConfigurationDAO CONFIGURATION_DAO = new ConfigurationDAO();
  @FXML
  private TextField fieldCurrentEvaluationEnabled;
  @FXML
  private ComboBox<StudentDTO> comboBoxStudent;
  @FXML
  private ComboBox<Integer> comboBoxAdequateUseGrade;
  @FXML
  private ComboBox<Integer> comboBoxContentCongruenceGrade;
  @FXML
  private ComboBox<Integer> comboBoxWritingGrade;
  @FXML
  private ComboBox<Integer> comboBoxMethodologicalRigorGrade;
  @FXML
  private TextArea textAreaFeedback;
  private ConfigurationDTO mostRecentEvaluationConfigurationDTO;
  private AcademicDTO currentAcademicDTO;

  private void setCurrentAcademicDTO(AcademicDTO currentAcademicDTO) {
    this.currentAcademicDTO = currentAcademicDTO;
  }

  private void setMostRecentEvaluationConfigurationDTO(ConfigurationDTO configurationDTO) {
    this.mostRecentEvaluationConfigurationDTO = configurationDTO;
  }

  private Optional<ConfigurationDTO> getMostRecentEvaluationConfigurationDTO() {
    return Optional.ofNullable(mostRecentEvaluationConfigurationDTO);
  }

  private Optional<AcademicDTO> getCurrentAcademicDTO() {
    return Optional.ofNullable(currentAcademicDTO);
  }

  private Optional<StudentDTO> getSelectedStudent() {
    return Optional.ofNullable(comboBoxStudent.getValue());
  }

  private ProjectDTO getCurrentStudentProjectDTO() throws UserDisplayableException, NotFoundException {
    if (getSelectedStudent().isEmpty()) {
      throw new UserDisplayableException("Debe seleccionar un estudiante para registrar la evaluación.");
    }

    return new ProjectDAO().getOneByStudent(getSelectedStudent().get().getID());
  }

  public void initialize() {
    try {
      AcademicDTO currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
      setCurrentAcademicDTO(currentAcademicDTO);

      ConfigurationDTO mostRecentEvaluationConfigurationDTO = getMostRecentEvaluationPeriod();
      setMostRecentEvaluationConfigurationDTO(mostRecentEvaluationConfigurationDTO);

      loadComboBoxStudent(currentAcademicDTO, mostRecentEvaluationConfigurationDTO);
      loadFieldCurrentEvaluationEnabled(mostRecentEvaluationConfigurationDTO);
      loadAllComboBoxGrades();
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No es posible registrar evaluación.", e.getMessage());
    }
  }

  private void loadAllComboBoxGrades() {
    ObservableList<Integer> grades = FXCollections.observableArrayList(
      IntStream.rangeClosed(1, 10).boxed().toList()
    );

    comboBoxAdequateUseGrade.setItems(grades);
    comboBoxContentCongruenceGrade.setItems(grades);
    comboBoxWritingGrade.setItems(grades);
    comboBoxMethodologicalRigorGrade.setItems(grades);
  }

  public void loadComboBoxStudent(
    AcademicDTO currentAcademicDTO,
    ConfigurationDTO mostRecentEvaluationConfigurationDTO
  ) throws UserDisplayableException {
    List<StudentDTO> studentList = STUDENT_DAO.getAllEvaluableStudentsByEvaluatorAndEvaluationPeriod(
      currentAcademicDTO.getID(),
      mostRecentEvaluationConfigurationDTO.name()
    );

    if (studentList.isEmpty()) {
      AlertFacade.showErrorAndWait("No hay estudiantes que puedan ser evaluados. Intente más tarde.");
      throw new UserDisplayableException("No hay estudiantes que puedan ser evaluados.");
    }

    comboBoxStudent.getItems().addAll(studentList);
    comboBoxStudent.setValue(studentList.getFirst());
  }

  public EvaluationDTO getEvaluationDTOFromFields(AcademicDTO currentAcademicDTO, ProjectDTO currentStudentProject) {
    return new EvaluationDTO.EvaluationBuilder()
      .setIDAcademic(currentAcademicDTO.getID())
      .setIDProject(currentStudentProject.getID())
      .setIDStudent(comboBoxStudent.getValue().getID())
      .setContentCongruenceGrade(String.valueOf(comboBoxContentCongruenceGrade.getValue()))
      .setMethodologicalRigorGrade(String.valueOf(comboBoxMethodologicalRigorGrade.getValue()))
      .setWritingGrade(String.valueOf(comboBoxWritingGrade.getValue()))
      .setAdequateUseGrade(String.valueOf(comboBoxAdequateUseGrade.getValue()))
      .setKind(mostRecentEvaluationConfigurationDTO.name().toEvaluationKind())
      .setFeedback(textAreaFeedback.getText())
      .build();
  }

  /**
   * Retrieves the most recent evaluation period that is enabled.
   * It checks the final, second, and first evaluation periods in that order.
   *
   * @return an Optional containing the most recent enabled ConfigurationDTO for evaluation, or empty if none are enabled.
   * @throws UserDisplayableException if there is an error retrieving the configurations.
   */
  private ConfigurationDTO getMostRecentEvaluationPeriod() throws UserDisplayableException {
    ConfigurationDTO finalEvaluationConfiguration = CONFIGURATION_DAO.getOne(ConfigurationName.EVALUATION_ENABLED_FINAL);

    if (finalEvaluationConfiguration.isEnabled()) {
      return finalEvaluationConfiguration;
    }

    ConfigurationDTO secondEvaluationConfiguration = CONFIGURATION_DAO.getOne(ConfigurationName.EVALUATION_ENABLED_SECOND);

    if (secondEvaluationConfiguration.isEnabled()) {
      return secondEvaluationConfiguration;
    }

    ConfigurationDTO firstEvaluationConfiguration = CONFIGURATION_DAO.getOne(ConfigurationName.EVALUATION_ENABLED_FIRST);

    if (firstEvaluationConfiguration.isEnabled()) {
      return firstEvaluationConfiguration;
    }

    throw new UserDisplayableException("No hay periodo de evaluación habilitado actualmente.");
  }

  /**
   * Loads the most recently enabled period of evaluation.
   * It sets fieldCurrentEvaluationEnabled with the name of the current evaluation enabled so that the user can see which period is currently enabled.
   */
  private void loadFieldCurrentEvaluationEnabled(
    ConfigurationDTO mostRecentEvaluationPeriod
  ) throws UserDisplayableException {
    fieldCurrentEvaluationEnabled.setText(mostRecentEvaluationPeriod.name().toString());
  }

  private void showAndWaitNoAvailableEvaluationPeriodAlert() {
    AlertFacade.showErrorAndWait(
      "No hay un periodo de evaluación habilitado actualmente. Por favor, contacte al coordinador para más información."
    );
  }

  private boolean verifyDuplicateEvaluation(EvaluationDTO evaluationDTO) throws UserDisplayableException {
    EvaluationDTO existingEvaluationDTO = EVALUATION_DAO.getOne(
      new FilterEvaluation(
        evaluationDTO.getIDProject(),
        evaluationDTO.getIDStudent(),
        evaluationDTO.getIDAcademic()
      )
    );

    if (existingEvaluationDTO != null) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible registrar evaluación debido a que ya existe una evaluación registrada para este estudiante en el proyecto."
      );
      return false;
    }

    return true;
  }

  private void registerEvaluationDTO(EvaluationDTO evaluationDTO) throws UserDisplayableException {
    EVALUATION_DAO.createOne(evaluationDTO);
    AlertFacade.showSuccessAndWait("La evaluación ha sido registrada exitosamente.");
  }

  public void handleRegister() {
    try {
      if (getCurrentAcademicDTO().isEmpty()) {
        AlertFacade.showErrorAndWait("No se ha podido obtener el académico actual.");
        return;
      }

      if (getMostRecentEvaluationConfigurationDTO().isEmpty()) {
        showAndWaitNoAvailableEvaluationPeriodAlert();
        return;
      }

      if (getSelectedStudent().isEmpty()) {
        AlertFacade.showErrorAndWait("Debe seleccionar un estudiante para registrar la evaluación.");
        return;
      }

      EvaluationDTO evaluationDTO = getEvaluationDTOFromFields(
        getCurrentAcademicDTO().get(),
        getCurrentStudentProjectDTO()
      );

      if (verifyDuplicateEvaluation(evaluationDTO)) {
        registerEvaluationDTO(evaluationDTO);
        return;
      }

      AlertFacade.showErrorAndWait("Ya existe una evaluación registrada para esta práctica.");
    } catch (IllegalArgumentException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar evaluación.", e.getMessage());
    }
  }
}
