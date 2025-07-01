package org.example.gui.combobox;

import javafx.scene.control.ComboBox;
import org.example.business.auth.AuthClient;
import org.example.business.dao.CourseDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.enumeration.Section;
import org.example.common.UserDisplayableException;

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
   * Loads the given ComboBox with courses associated with the current Academic user logged in the system.
   * If no courses are found, it shows an error alert.
   * <p>
   * Throws <code>NotFoundException</code> when unable to retrieve the current Academic.
   * Throws <code>UserDisplayableException</code> when there are no courses associated with the current Academic.
   *
   * @param comboBoxCourse The ComboBox to be populated with CourseDTO items.
   */
  public static void loadByCurrentAcademicDTO(ComboBox<CourseDTO> comboBoxCourse) throws NotFoundException, UserDisplayableException {
    List<CourseDTO> courseList = COURSE_DAO.getAllByAcademic(
      AuthClient.getInstance().getCurrentAcademicDTO().getID()
    );

    if (courseList.isEmpty()) {
      throw new UserDisplayableException(
        "No existen cursos asignados a su cuenta. Por favor, contacte a su Coordinador."
      );
    }

    comboBoxCourse.getItems().addAll(courseList);
    comboBoxCourse.setValue(courseList.getFirst());
  }

  /**
   * Loads the given ComboBox with all available sections.
   * The default value is set to Section.S1.
   *
   * @param comboBoxSection The ComboBox to be populated with Section items.
   */
  public static void loadComboBoxSection(ComboBox<Section> comboBoxSection) {
    comboBoxSection.getItems().addAll(Section.values());
    comboBoxSection.setValue(Section.S1);
  }
}