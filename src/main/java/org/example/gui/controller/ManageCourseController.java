package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.AcademicDAO;
import org.example.business.dao.CourseDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.enumeration.CourseState;
import org.example.business.dto.enumeration.Section;
import org.example.business.dto.enumeration.Semester;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class ManageCourseController extends ManageController<CourseDTO> {
  private final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  private final CourseDAO COURSE_DAO = new CourseDAO();
  @FXML
  private TextField fieldNRC;
  @FXML
  private ComboBox<AcademicDTO> comboBoxAcademic;
  @FXML
  private ComboBox<Section> comboBoxSection;
  @FXML
  private ComboBox<Semester> comboBoxSemester;
  @FXML
  private ComboBox<CourseState> comboBoxState;

  @Override
  public void initialize(CourseDTO dataObject) {
    super.initialize(dataObject);
    RegisterCourseController.loadComboBoxAcademic(comboBoxAcademic);
    RegisterCourseController.loadComboBoxSection(comboBoxSection);
    RegisterCourseController.loadComboBoxSemester(comboBoxSemester);
    loadComboboxState();
    loadDataObjectFields();
  }

  private void loadComboboxState() {
    comboBoxState.getItems().addAll(CourseState.values());
    comboBoxState.setValue(CourseState.ON_GOING);
  }

  public void loadDataObjectFields() {
    fieldNRC.setText(getContext().getNRC());

    loadAcademic();

    comboBoxSection.setValue(getContext().getSection());
    comboBoxSemester.setValue(getContext().getSemester());
    comboBoxState.setValue(getContext().getState());
  }

  private void loadAcademic() {
    try {
      for (AcademicDTO academicDTO : comboBoxAcademic.getItems()) {
        if (academicDTO.getEmail().equals(getContext().getIDAcademic())) {
          comboBoxAcademic.setValue(academicDTO);
          return;
        }
      }

      AcademicDTO organization = ACADEMIC_DAO.getOne(getContext().getIDAcademic());
      comboBoxAcademic.setValue(organization);
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      CourseDTO courseDTO = new CourseDTO.CourseBuilder()
        .setNRC(fieldNRC.getText())
        .setIDAcademic(comboBoxAcademic.getValue().getID())
        .setSection(comboBoxSection.getValue())
        .setSemester(comboBoxSemester.getValue())
        .setState(comboBoxState.getValue())
        .build();

      COURSE_DAO.updateOne(courseDTO);
      AlertFacade.showSuccessAndWait("El curso ha sido actualizado exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}