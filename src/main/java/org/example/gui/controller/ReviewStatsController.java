package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.example.business.dao.NotFoundException;
import org.example.business.dao.StatsDAO;
import org.example.business.dto.StatsDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class ReviewStatsController extends Controller {
  private final StatsDAO STATS_DAO = new StatsDAO();
  @FXML
  private Text textTotalAcademics;
  @FXML
  private Text textTotalEvaluators;
  @FXML
  private Text textTotalOrganizations;
  @FXML
  private Text textTotalProjectRequests;
  @FXML
  private Text textTotalEvaluations;
  @FXML
  private Text textTotalSelfEvaluations;
  @FXML
  private Text textTotalMonthlyReports;
  @FXML
  private Text textTotalProjects;
  @FXML
  private Text textTotalCourses;
  @FXML
  private Text textTotalStudents;

  public void initialize() {
    loadStatsData();
  }

  public void loadStatsData() {
    try {
      StatsDTO statsDTO = STATS_DAO.getStats();

      textTotalAcademics.setText(String.valueOf(statsDTO.getTotalAcademics()));
      textTotalEvaluators.setText(String.valueOf(statsDTO.getTotalEvaluators()));
      textTotalOrganizations.setText(String.valueOf(statsDTO.getTotalOrganizations()));
      textTotalProjectRequests.setText(String.valueOf(statsDTO.getTotalProjectRequests()));
      textTotalEvaluations.setText(String.valueOf(statsDTO.getTotalEvaluations()));
      textTotalSelfEvaluations.setText(String.valueOf(statsDTO.getTotalSelfEvaluations()));
      textTotalMonthlyReports.setText(String.valueOf(statsDTO.getTotalMonthlyReports()));
      textTotalProjects.setText(String.valueOf(statsDTO.getTotalProjects()));
      textTotalCourses.setText(String.valueOf(statsDTO.getTotalCourses()));
      textTotalStudents.setText(String.valueOf(statsDTO.getTotalStudents()));
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
