package org.example.gui.combobox;

import javafx.scene.control.ComboBox;
import org.example.business.auth.AuthClient;
import org.example.business.dao.CourseDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.CourseDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.util.List;

/**
 * CourseComboBoxLoader is a utility class that provides methods to load a ComboBox with CourseDTO items
 * associated with the current academic year.
 * <p>
 * It handles exceptions related to course retrieval and displays appropriate error messages.
 */
public class CourseComboBoxLoader {
  private static final CourseDAO COURSE_DAO = new CourseDAO();

  /**
   * Loads the course combo box with courses associated with the current academic year.
   * If no courses are found, it shows an error alert.
   * <p>
   * <code>NotFoundException</code> and <code>UserDisplayableException</code> are handled by this method.
   *
   * @param comboBoxCourse The ComboBox to be populated with CourseDTO items.
   */
  public static void loadByCurrentAcademicDTO(ComboBox<CourseDTO> comboBoxCourse) {
    try {
      List<CourseDTO> courseList = COURSE_DAO.getAllByAcademic(
        AuthClient.getInstance().getCurrentAcademicDTO().getID()
      );

      if (courseList.isEmpty()) {
        AlertFacade.showErrorAndWait("No existe un curso activo asignado a ústed. Por favor, contacte a su Coordinador.");
        return;
      }

      comboBoxCourse.getItems().addAll(courseList);
      comboBoxCourse.setValue(courseList.getFirst());
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}