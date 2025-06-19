package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.AcademicDAO;
import org.example.business.dao.CourseDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.CourseDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ManageCourseController extends ManageController<CourseDTO> {
  private final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  private final CourseDAO COURSE_DAO = new CourseDAO();
  @FXML
  private TextField fieldNRC;
  @FXML
  private ComboBox<AcademicDTO> comboBoxAcademic;
  @FXML
  private ComboBox<CourseDTO.Section> comboBoxSection;
  @FXML
  private ComboBox<CourseDTO.Semester> comboBoxSemester;
  @FXML
  private ComboBox<CourseDTO.State> comboBoxState;

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
    comboBoxState.getItems().addAll(CourseDTO.State.values());
    comboBoxState.setValue(CourseDTO.State.ON_GOING);
  }

  public void loadDataObjectFields() {
    fieldNRC.setText(getCurrentDataObject().getNRC());

    loadAcademic();

    comboBoxSection.setValue(getCurrentDataObject().getSection());
    comboBoxSemester.setValue(getCurrentDataObject().getSemester());
    comboBoxState.setValue(getCurrentDataObject().getState());
  }

  private void loadAcademic() {
    try {
      for (AcademicDTO academicDTO : comboBoxAcademic.getItems()) {
        if (academicDTO.getEmail().equals(getCurrentDataObject().getIDAcademic())) {
          comboBoxAcademic.setValue(academicDTO);
          break;
        }
      }

      AcademicDTO organization = ACADEMIC_DAO.getOne(getCurrentDataObject().getIDAcademic());
      comboBoxAcademic.setValue(organization);
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar la organización del proyecto debido a un error en el sistema.");
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
      Modal.displaySuccess("El curso ha sido actualizado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar curso debido a un error en el sistema.");
    }
  }
}