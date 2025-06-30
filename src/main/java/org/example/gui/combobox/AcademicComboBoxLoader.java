package org.example.gui.combobox;

import javafx.scene.control.ComboBox;
import org.example.business.dto.enumeration.AcademicRole;

public class AcademicComboBoxLoader {
  /**
   * Loads the given ComboBox with all available AcademicRole values and sets the default value to ACADEMIC.
   *
   * @param comboBoxRole The ComboBox to be populated with AcademicRole items.
   */
  public static void loadAcademicRoleComboBox(ComboBox<AcademicRole> comboBoxRole) {
    comboBoxRole.getItems().addAll(AcademicRole.values());
    comboBoxRole.setValue(AcademicRole.ACADEMIC);
  }
}
