package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dto.ConfigurationDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ManageConfigurationController extends Controller {
  private static final ConfigurationDAO CONFIGURATION_DAO = new ConfigurationDAO();
  @FXML
  CheckBox checkBoxEnableEvaluation;

  public void initialize() {
    try {
      ConfigurationDTO configuration = CONFIGURATION_DAO.getOne("EVALUATION-ENABLED");

      if (configuration == null) {
        CONFIGURATION_DAO.createOne(new ConfigurationDTO("EVALUATION-ENABLED", "false"));
        configuration = CONFIGURATION_DAO.getOne("EVALUATION-ENABLED");
      }

      checkBoxEnableEvaluation.setSelected(Boolean.parseBoolean(configuration.value()));
    } catch (Exception e) {
      Modal.displayError("No ha sido posible cargar la configuración de evaluaciones de presentaciones debido a error en el sistema");
    }
  }

  public void handleManageConfiguration() {
    boolean isSelected = checkBoxEnableEvaluation.isSelected();

    try {
      CONFIGURATION_DAO.updateOne(new ConfigurationDTO("EVALUATION-ENABLED", String.valueOf(isSelected)));
      Modal.displaySuccess("Se ha actualizado la configuración del sistema exitosamente");
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar la configuración del sistema debido a un error de sistema");
    }
  }
}
