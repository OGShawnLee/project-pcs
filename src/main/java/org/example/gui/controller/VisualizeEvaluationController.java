package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.business.dto.EvaluationDTO;

public class VisualizeEvaluationController extends ManageController<EvaluationDTO>{

  @FXML
  private Label labelProject;
  @FXML
  private Label labelIDStudent;
  @FXML
  private Label labelIDAcademic;
  @FXML
  private Label labelMethodologicalRigorGrade;
  @FXML
  private Label labelContentCongruenceGrade;
  @FXML
  private Label labelWritingGrade;
  @FXML
  private Label labelAdequateUseGrade;
  @FXML
  private TextArea textAreaFeedback;

  @Override
  public void initialize(EvaluationDTO dataObject) {
    super.initialize(dataObject);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    labelProject.setText(String.valueOf(getCurrentDataObject().getIDProject()));
    labelIDStudent.setText(getCurrentDataObject().getIDStudent());
    labelIDAcademic.setText(getCurrentDataObject().getIDAcademic());
    labelMethodologicalRigorGrade.setText(String.valueOf(getCurrentDataObject().getMethodologicalRigorGrade()));
    labelContentCongruenceGrade.setText(String.valueOf(getCurrentDataObject().getContentCongruenceGrade()));
    labelWritingGrade.setText(String.valueOf(getCurrentDataObject().getWritingGrade()));
    labelAdequateUseGrade.setText(String.valueOf(getCurrentDataObject().getAdequateUseGrade()));

    textAreaFeedback.setText(getCurrentDataObject().getFeedback());
  }

  @Override
  protected void handleUpdateCurrentDataObject() {

  }
}