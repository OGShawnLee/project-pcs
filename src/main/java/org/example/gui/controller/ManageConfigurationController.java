package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import org.example.business.dao.Configuration;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dto.ConfigurationDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ManageConfigurationController extends Controller {
  private static final ConfigurationDAO CONFIGURATION_DAO = new ConfigurationDAO();
  @FXML
  CheckBox checkBoxEnableFirstEvaluation;
  @FXML
  CheckBox checkBoxEnableSecondEvaluation;
  @FXML
  CheckBox checkBoxEnableFinalEvaluation;

  public void initialize() {
    loadConfiguration();
  }

  public void loadConfiguration() {
    try {
      ConfigurationDTO firstEvaluationConfig = CONFIGURATION_DAO.getOne(Configuration.EVALUATION_ENABLED_FIRST);
      ConfigurationDTO secondEvaluationConfig = CONFIGURATION_DAO.getOne(Configuration.EVALUATION_ENABLED_SECOND);
      ConfigurationDTO lastEvaluationConfig = CONFIGURATION_DAO.getOne(Configuration.EVALUATION_ENABLED_FINAL);

      checkBoxEnableFirstEvaluation.setSelected(firstEvaluationConfig.isEnabled());
      checkBoxEnableSecondEvaluation.setSelected(secondEvaluationConfig.isEnabled());
      checkBoxEnableFinalEvaluation.setSelected(lastEvaluationConfig.isEnabled());
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar la configuración de evaluaciones de presentaciones debido a error en el sistema"
      );
    }
  }

  public void handleManageConfiguration() {
    try {
      CONFIGURATION_DAO.updateOne(
        new ConfigurationDTO(Configuration.EVALUATION_ENABLED_FIRST, checkBoxEnableFirstEvaluation.isSelected())
      );
      CONFIGURATION_DAO.updateOne(
        new ConfigurationDTO(Configuration.EVALUATION_ENABLED_SECOND, checkBoxEnableSecondEvaluation.isSelected())
      );
      CONFIGURATION_DAO.updateOne(
        new ConfigurationDTO(Configuration.EVALUATION_ENABLED_FINAL, checkBoxEnableFinalEvaluation.isSelected())
      );
      Modal.displaySuccess("Se ha actualizado la configuración del sistema exitosamente");
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible actualizar la configuración del sistema debido a un error de sistema"
      );
    }
  }
}
