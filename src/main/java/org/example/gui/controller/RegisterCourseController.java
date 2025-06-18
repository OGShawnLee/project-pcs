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
import java.util.List;

public class RegisterCourseController extends Controller {
  private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  private static final CourseDAO COURSE_DAO = new CourseDAO();
  @FXML
  private TextField fieldNRC;
  @FXML
  private ComboBox<AcademicDTO> comboBoxAcademic;
  @FXML
  private ComboBox<CourseDTO.Section> comboBoxSection;
  @FXML
  private ComboBox<CourseDTO.Semester> comboBoxSemester;

  public void initialize() {
    loadComboBoxSection(comboBoxSection);
    loadComboBoxSemester(comboBoxSemester);
    loadComboBoxAcademic(comboBoxAcademic);
  }

  public static void loadComboBoxSection(ComboBox<CourseDTO.Section> comboBoxSection) {
    comboBoxSection.getItems().addAll(CourseDTO.Section.values());
    comboBoxSection.setValue(CourseDTO.Section.S1);
  }

  public static void loadComboBoxSemester(ComboBox<CourseDTO.Semester> comboBoxSemester) {
    comboBoxSemester.getItems().addAll(CourseDTO.Semester.values());
    comboBoxSemester.setValue(CourseDTO.Semester.FEB_JUL);
  }

  public static void loadComboBoxAcademic(ComboBox<AcademicDTO> comboBoxAcademic) {
    try {
      List<AcademicDTO> academicList = ACADEMIC_DAO.getAllByState("ACTIVE");

      if (academicList.isEmpty()) {
        Modal.displayError(
          "No es posible registrar un curso porque no hay ningún académico que se le pueda asignar registrado en el sistema."
        );
        Modal.display(
          "Registrar Académico",
          "RegisterAcademicModal",
          () -> loadComboBoxAcademic(comboBoxAcademic)
        );
        return;
      }

      comboBoxAcademic.getItems().addAll(academicList);
      comboBoxAcademic.setValue(academicList.get(0));
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
    }
  }

  public void handleRegister() {
    try {
      CourseDTO duplicateCourse = COURSE_DAO.getOne(fieldNRC.getText());

      if (duplicateCourse != null) {
        Modal.displayError("No ha sido posible registrar el curso porque ya existe un curso con el NRC ingresado.");
        return;
      }

      CourseDTO courseDTO = new CourseDTO.CourseBuilder()
        .setNRC(fieldNRC.getText())
        .setIDAcademic(comboBoxAcademic.getValue().getID())
        .setSection(comboBoxSection.getValue())
        .setSemester(comboBoxSemester.getValue())
        .build();

      COURSE_DAO.createOne(courseDTO);
      Modal.displaySuccess("El curso ha sido registrado exitosamente.");
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible registrar el curso debido a un error en el sistema.");
    }
  }
}
