package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.example.business.dto.WorkPlanDTO;

public class ReviewWorkPlanController extends ContextController<WorkPlanDTO> {
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
    fieldProjectGoal.setText(getContext().getProjectGoal());
    fieldTheoreticalScope.setText(getContext().getTheoreticalScope());
    fieldFirstMonthActivities.setText(getContext().getFirstMonthActivities());
    fieldSecondMonthActivities.setText(getContext().getSecondMonthActivities());
    fieldThirdMonthActivities.setText(getContext().getThirdMonthActivities());
    fieldFourthMonthActivities.setText(getContext().getFourthMonthActivities());
  }
}
