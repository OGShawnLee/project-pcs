package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.AcademicDAO;
import org.example.business.dao.CourseDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.enumeration.Section;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.combobox.AcademicComboBoxLoader;
import org.example.gui.combobox.CourseComboBoxLoader;

public class RegisterCourseController extends Controller {
  private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  private static final CourseDAO COURSE_DAO = new CourseDAO();
  @FXML
  private TextField fieldNRC;
  @FXML
  private ComboBox<AcademicDTO> comboBoxAcademic;
  @FXML
  private ComboBox<Section> comboBoxSection;

  public void initialize() {
    CourseComboBoxLoader.loadComboBoxSection(comboBoxSection);
    AcademicComboBoxLoader.loadComboBoxAcademic(comboBoxAcademic);
  }

  private CourseDTO createCourseDTOFromFields() {
    return new CourseDTO.CourseBuilder()
      .setNRC(fieldNRC.getText())
      .setIDAcademic(comboBoxAcademic.getValue().getID())
      .setSection(comboBoxSection.getValue())
      .build();
  }

  /**
   * Verifies if a course with the given NRC already exists and shows an error if it does.
   *
   * @return true if no duplicate course exists, false otherwise.
   * @throws UserDisplayableException if an error occurs while checking for duplicates.
   */
  public boolean verifyDuplicateCourse() throws UserDisplayableException {
    CourseDTO existingCourseDTO = COURSE_DAO.getOne(fieldNRC.getText());

    if (existingCourseDTO != null) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible registrar el curso porque ya existe un curso con el NRC ingresado."
      );
      return false;
    }

    return true;
  }

  public void updateCourseDTO() throws UserDisplayableException {
      COURSE_DAO.createOne(createCourseDTOFromFields());
      AlertFacade.showSuccessAndWait("El curso ha sido registrado exitosamente.");
  }

  public void handleRegister() {
    try {
      if (verifyDuplicateCourse()) {
        updateCourseDTO();
      }
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar curso", e.getMessage());
    }
  }
}
