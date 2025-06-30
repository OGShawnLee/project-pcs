package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.CourseDAO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.*;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

// TODO: UPDATE USE CASE
public class ManageStudentController extends ManageController<StudentDTO> {
  private final StudentDAO STUDENT_DAO = new StudentDAO();
  private final CourseDAO COURSE_DAO = new CourseDAO();
  private final EnrollmentDAO ENROLLMENT_DAO = new EnrollmentDAO();
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldIDStudent;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldPhoneNumber;
  @FXML
  private ComboBox<EnrollmentDTO> comboBoxNRC;
  @FXML
  private ComboBox<String> comboBoxState;

  @Override
  public void initialize(StudentDTO dataObject) {
    super.initialize(dataObject);
    ComboBoxLoader.loadComboBoxState(comboBoxState);
    loadEnrollments(); // <-- cargar ítems primero
    loadDataObjectFields();
  }

  public void loadEnrollments() {

  }

  public void loadDataObjectFields() {
    StudentDTO student = getContext();

    fieldIDStudent.setText(student.getID());
    fieldEmail.setText(student.getEmail());
    fieldName.setText(student.getName());
    fieldPaternalLastName.setText(student.getPaternalLastName());
    fieldMaternalLastName.setText(student.getMaternalLastName());
    fieldPhoneNumber.setText(student.getPhoneNumber());
    comboBoxState.setValue(student.getState());

    try {
      for (EnrollmentDTO enrollment : comboBoxNRC.getItems()) {
        if (enrollment.getIDStudent().equals(student.getID())) {
          comboBoxNRC.setValue(enrollment);
          break;
        }
      }
    } catch (Exception e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar la inscripción del estudiante.");
    }
  }

  private StudentDTO createStudentDTOFromFields() {
    return new StudentDTO.StudentBuilder()
      .setID(fieldIDStudent.getText())
      .setEmail(fieldEmail.getText())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setState(comboBoxState.getValue())
      .build();
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      STUDENT_DAO.updateOne(createStudentDTOFromFields());
      AlertFacade.showSuccessAndWait("El estudiante ha sido actualizado exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}