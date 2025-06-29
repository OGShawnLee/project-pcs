package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import org.example.business.dto.enumeration.ConfigurationName;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dto.ConfigurationDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

public class ManageConfigurationController extends Controller {
  private static final ConfigurationDAO CONFIGURATION_DAO = new ConfigurationDAO();
  @FXML
  private CheckBox checkBoxEnableFirstEvaluation;
  @FXML
  private CheckBox checkBoxEnableSecondEvaluation;
  @FXML
  private CheckBox checkBoxEnableFinalEvaluation;

  public void initialize() {
    loadConfiguration();
  }

  public void loadConfiguration() {
    try {
      ConfigurationDTO firstEvaluationConfig = CONFIGURATION_DAO.getOne(ConfigurationName.EVALUATION_ENABLED_FIRST);
      ConfigurationDTO secondEvaluationConfig = CONFIGURATION_DAO.getOne(ConfigurationName.EVALUATION_ENABLED_SECOND);
      ConfigurationDTO lastEvaluationConfig = CONFIGURATION_DAO.getOne(ConfigurationName.EVALUATION_ENABLED_FINAL);

      checkBoxEnableFirstEvaluation.setSelected(firstEvaluationConfig.isEnabled());
      checkBoxEnableSecondEvaluation.setSelected(secondEvaluationConfig.isEnabled());
      checkBoxEnableFinalEvaluation.setSelected(lastEvaluationConfig.isEnabled());
    } catch (UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }

  public void handleManageConfiguration() {
    try {
      CONFIGURATION_DAO.updateOne(
        new ConfigurationDTO(ConfigurationName.EVALUATION_ENABLED_FIRST, checkBoxEnableFirstEvaluation.isSelected())
      );
      CONFIGURATION_DAO.updateOne(
        new ConfigurationDTO(ConfigurationName.EVALUATION_ENABLED_SECOND, checkBoxEnableSecondEvaluation.isSelected())
      );
      CONFIGURATION_DAO.updateOne(
        new ConfigurationDTO(ConfigurationName.EVALUATION_ENABLED_FINAL, checkBoxEnableFinalEvaluation.isSelected())
      );
      Modal.displaySuccess("Se ha actualizado la configuración del sistema exitosamente");
    } catch (UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }
}
