package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.auth.AuthClient;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.EnrollmentDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.CourseDAO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.StudentDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

import java.util.List;

public class RegisterStudentController extends Controller {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
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
  private ComboBox<CourseDTO> comboBoxNRC;

  public void initialize() {
    loadComboBoxCourse();
  }

  public void loadComboBoxCourse() {
    try {
      AcademicDTO currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
      List<CourseDTO> courseList = COURSE_DAO.getAllByAcademic(currentAcademicDTO.getID());

      if (courseList.isEmpty()) {
        Modal.displayError("No existe un curso activo asignado a ústed. Por favor, contacte a su Coordinador.");
        return;
      }

      comboBoxNRC.getItems().addAll(courseList);
      comboBoxNRC.setValue(courseList.getFirst());
    } catch (UserDisplayableException e) {
      Modal.displayError(e.getMessage());
    }
  }

  public void handleRegister() {
    try {
      StudentDTO studentDTO = new StudentDTO.StudentBuilder()
        .setID(fieldIDStudent.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setPhoneNumber(fieldPhoneNumber.getText())
        .build();

      AccountDTO existingAccountDTO = ACCOUNT_DAO.getOne(studentDTO.getEmail());
      if (existingAccountDTO != null) {
        Modal.displayError("No ha sido posible registrar estudiante debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      StudentDTO existingStudentDTO = STUDENT_DAO.getOne(studentDTO.getID());
      if (existingStudentDTO != null) {
        Modal.displayError("No ha sido posible registrar estudiante debido a que ya existe un estudiante con la misma matrícula.");
        return;
      }

      STUDENT_DAO.createOne(studentDTO);

      EnrollmentDTO enrollmentDTO = new EnrollmentDTO.EnrollmentBuilder()
        .setIDAcademic(AuthClient.getInstance().getCurrentAcademicDTO().getID())
        .setIDStudent(fieldIDStudent.getText())
        .setIDCourse(comboBoxNRC.getValue().getNRC())
        .build();

      ENROLLMENT_DAO.createOne(enrollmentDTO);
      Modal.displaySuccess("El estudiante ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible registrar estudiante.",  e);
    }
  }

}