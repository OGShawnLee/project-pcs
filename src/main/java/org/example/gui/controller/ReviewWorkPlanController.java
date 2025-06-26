package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.example.business.dto.WorkPlanDTO;

public class ReviewWorkPlanController extends ViewController<WorkPlanDTO> {
  @FXML
  public Text fieldProjectGoal;
  @FXML
  public Text fieldTheoreticalScope;
  @FXML
  public Text fieldFirstMonthActivities;
  @FXML
  public Text fieldSecondMonthActivities;
  @FXML
  public Text fieldThirdMonthActivities;
  @FXML
  public Text fieldFourthMonthActivities;

  @Override
  public void initialize(WorkPlanDTO dataObject) {
    super.initialize(dataObject);
    loadFields();
  }

  private void loadFields() {
    fieldProjectGoal.setText(getCurrentDataObject().getProjectGoal());
    fieldTheoreticalScope.setText(getCurrentDataObject().getTheoreticalScope());
    fieldFirstMonthActivities.setText(getCurrentDataObject().getFirstMonthActivities());
    fieldSecondMonthActivities.setText(getCurrentDataObject().getSecondMonthActivities());
    fieldThirdMonthActivities.setText(getCurrentDataObject().getThirdMonthActivities());
    fieldFourthMonthActivities.setText(getCurrentDataObject().getFourthMonthActivities());
  }
}
