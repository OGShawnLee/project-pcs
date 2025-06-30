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
import org.example.gui.combobox.CourseComboBoxLoader;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

import java.util.List;

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
    loadComboBoxAcademic(comboBoxAcademic);
  }

  public static void loadComboBoxAcademic(ComboBox<AcademicDTO> comboBoxAcademic) {
    try {
      List<AcademicDTO> academicList = ACADEMIC_DAO.getAllByState("ACTIVE");

      if (academicList.isEmpty()) {
        AlertFacade.showErrorAndWait(
          "No es posible registrar un curso porque no hay ningún académico que se le pueda asignar registrado en el sistema."
        );
        ModalFacade.createAndDisplay(
          new ModalFacadeConfiguration(
            "Registrar Académico",
            "RegisterAcademicModal",
            () -> loadComboBoxAcademic(comboBoxAcademic)
          )
        );
        return;
      }

      comboBoxAcademic.getItems().addAll(academicList);
      comboBoxAcademic.setValue(academicList.get(0));
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  private CourseDTO createCourseDTOFromFields() {
    return new CourseDTO.CourseBuilder()
      .setNRC(fieldNRC.getText())
      .setIDAcademic(comboBoxAcademic.getValue().getID())
      .setSection(comboBoxSection.getValue())
      .build();
  }

  public void handleRegister() {
    try {
      CourseDTO existingCourseDTO = COURSE_DAO.getOne(fieldNRC.getText());

      if (existingCourseDTO != null) {
        AlertFacade.showErrorAndWait("No ha sido posible registrar el curso porque ya existe un curso con el NRC ingresado.");
        return;
      }

      COURSE_DAO.createOne(createCourseDTOFromFields());
      AlertFacade.showSuccessAndWait("El curso ha sido registrado exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
