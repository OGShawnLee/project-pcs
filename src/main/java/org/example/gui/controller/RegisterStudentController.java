package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.auth.AuthClient;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.EnrollmentDTO;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.combobox.CourseComboBoxLoader;

public class RegisterStudentController extends Controller {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  private final StudentDAO STUDENT_DAO = new StudentDAO();
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
  private ComboBox<CourseDTO> comboBoxCourse;

  public void initialize() {
    try {
      CourseComboBoxLoader.loadByCurrentAcademicDTO(comboBoxCourse);
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No es posible registrar estudiantes.", e.getMessage());
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
      .build();
  }

  private EnrollmentDTO createEnrollmentDTOFromFields() throws NotFoundException, UserDisplayableException {
    if (comboBoxCourse.getItems().isEmpty()) {
      throw new UserDisplayableException("No existen cursos asignados a su cuenta. Por favor, contacte a su Coordinador.");
    }

    return new EnrollmentDTO.EnrollmentBuilder()
      .setIDAcademic(AuthClient.getInstance().getCurrentAcademicDTO().getID())
      .setIDStudent(fieldIDStudent.getText())
      .setIDCourse(comboBoxCourse.getValue().getNRC())
      .build();
  }

  public void handleRegister() {
    try {
      StudentDTO studentDTO = createStudentDTOFromFields();

      AccountDTO existingAccountDTO = ACCOUNT_DAO.getOne(studentDTO.getEmail());
      if (existingAccountDTO != null) {
        AlertFacade.showErrorAndWait("No ha sido posible registrar estudiante debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      StudentDTO existingStudentDTO = STUDENT_DAO.getOne(studentDTO.getID());
      if (existingStudentDTO != null) {
        AlertFacade.showErrorAndWait("No ha sido posible registrar estudiante debido a que ya existe un estudiante con la misma matrícula.");
        return;
      }

      STUDENT_DAO.createOne(studentDTO);
      ENROLLMENT_DAO.createOne(createEnrollmentDTOFromFields());
      AlertFacade.showSuccessAndWait("El estudiante ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar estudiante.", e.getMessage());
    }
  }
}