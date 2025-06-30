package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.example.business.dao.NotFoundException;
import org.example.business.dao.ProjectDAO;
import org.example.business.dao.WorkPlanDAO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.WorkPlanDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

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

  public static void displayAsContextModal(StudentDTO currentStudentDTO) {
    try {
      ProjectDTO currentProjectDTO = new ProjectDAO().getOneByStudent(currentStudentDTO.getID());

      if (currentProjectDTO == null) {
        AlertFacade.showErrorAndWait("No se ha encontrado un proyecto asignado al estudiante actual.");
        return;
      }

      WorkPlanDTO currentWorkPlanDTO = new WorkPlanDAO().getOne(currentProjectDTO.getID());

      ModalFacade.createAndDisplayContextModal(
        new ModalFacadeConfiguration("Consultar Plan de Trabajo", "ReviewWorkPlanModal"),
        currentWorkPlanDTO
      );
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar el plan de trabajo.", e.getMessage());
    }
  }
}
