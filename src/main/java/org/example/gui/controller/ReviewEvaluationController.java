package org.example.gui.controller;

import javafx.fxml.FXML;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.example.business.dao.ProjectDAO;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dto.EvaluationPreviewDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class ReviewEvaluationController extends ContextController<EvaluationPreviewDTO> {
  @FXML
  private TextField fieldKind;
  @FXML
  private TextField fieldStudent;
  @FXML
  private TextField fieldAcademic;
  @FXML
  private TextArea textAreaProjectName;
  @FXML
  private TextField fieldAdequateUseGrade;
  @FXML
  private TextField fieldContentCongruenceGrade;
  @FXML
  private TextField fieldWritingGrade;
  @FXML
  private TextField fieldMethodologicalRigorGrade;
  @FXML
  private TextField fieldAverage;
  @FXML
  private TextArea textAreaFeedback;
  @FXML
  private TextField fieldCreatedAt;

  @Override
  public void initialize(EvaluationPreviewDTO evaluationPreviewDTO) {
    super.initialize(evaluationPreviewDTO);
    try {
      loadDataObjectFields();
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible consultar evalución", e.getMessage());
    }
  }

  private String getProjectName() throws UserDisplayableException {
    return new ProjectDAO().getOne(getContext().evaluationDTO().getIDProject()).getName();
  }

  private void loadDataObjectFields() throws UserDisplayableException {
    EvaluationDTO evaluationDTO = getContext().evaluationDTO();

    fieldKind.setText(evaluationDTO.getKind().toString());
    fieldStudent.setText(getContext().fullNameStudent());
    fieldAcademic.setText(getContext().fullNameAcademic());
    textAreaProjectName.setText(getProjectName());
    fieldAdequateUseGrade.setText(String.valueOf(evaluationDTO.getAdequateUseGrade()));
    fieldContentCongruenceGrade.setText(String.valueOf(evaluationDTO.getContentCongruenceGrade()));
    fieldWritingGrade.setText(String.valueOf(evaluationDTO.getWritingGrade()));
    fieldMethodologicalRigorGrade.setText(String.valueOf(evaluationDTO.getMethodologicalRigorGrade()));
    fieldAverage.setText(String.valueOf(evaluationDTO.getAverageGrade()));
    textAreaFeedback.setText(evaluationDTO.getFeedback());
    fieldCreatedAt.setText(evaluationDTO.getFormattedCreatedAt());
  }
}