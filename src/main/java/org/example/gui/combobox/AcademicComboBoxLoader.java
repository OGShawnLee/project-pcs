package org.example.gui.combobox;

import javafx.scene.control.ComboBox;

import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.enumeration.AcademicRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.util.List;

public class AcademicComboBoxLoader {
  private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();

  /**
   * Loads the given ComboBox with all available AcademicRole values and sets the default value to ACADEMIC.
   *
   * @param comboBoxRole The ComboBox to be populated with AcademicRole items.
   */
  public static void loadAcademicRoleComboBox(ComboBox<AcademicRole> comboBoxRole) {
    comboBoxRole.getItems().addAll(AcademicRole.values());
    comboBoxRole.setValue(AcademicRole.ACADEMIC);
  }

  /**
   * Loads the given ComboBox with active AcademicDTO items.
   * If no active academics are found, it shows an error alert and opens a modal to register a new academic.
   *
   * @param comboBoxAcademic The ComboBox to be populated with AcademicDTO items.
   */
  public static void loadComboBoxAcademic(ComboBox<AcademicDTO> comboBoxAcademic) {
    try {
      List<AcademicDTO> academicList = ACADEMIC_DAO.getAllByState("ACTIVE");

      if (academicList.isEmpty()) {
        AlertFacade.showErrorAndWait(
          "No es posible registrar un curso porque no hay ningún académico que se le pueda asignar registrado en el sistema."
        );
        return;
      }

      comboBoxAcademic.getItems().addAll(academicList);
      comboBoxAcademic.setValue(academicList.get(0));
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar académicos.", e.getMessage());
    }
  }
}
