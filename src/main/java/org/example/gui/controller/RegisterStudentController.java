package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dto.*;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.CourseDAO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class RegisterStudentController extends Controller{
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
    private TextField fieldIdStudent;
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
            List<CourseDTO> courseList = COURSE_DAO.getAll();

            if (courseList.isEmpty()) {
                Modal.displayError("No existe un curso.");
                return;
            }

            comboBoxNRC.getItems().addAll(courseList);
            comboBoxNRC.setValue(courseList.getFirst());
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
        }
    }

    public void handleRegister() {
        try {
            StudentDTO studentDTO = new StudentDTO.StudentBuilder()
                    .setID(fieldIdStudent.getText())
                    .setEmail(fieldEmail.getText())
                    .setName(fieldName.getText())
                    .setPaternalLastName(fieldPaternalLastName.getText())
                    .setMaternalLastName(fieldMaternalLastName.getText())
                    .setPhoneNumber(fieldPhoneNumber.getText())
                    .build();



            AccountDTO existingAccountDTO = ACCOUNT_DAO.getOne(studentDTO.getEmail());
            if (existingAccountDTO != null) {
                Modal.displayError("No ha sido posible registrar académico debido a que ya existe una cuenta con ese correo electrónico.");
                return;
            }

            StudentDTO existingStudentDTO = STUDENT_DAO.getOne(studentDTO.getID());
            if (existingStudentDTO != null) {
                Modal.displayError("No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador.");
                return;
            }

            STUDENT_DAO.createOne(studentDTO);

            EnrollmentDTO enrollmentDTO = new EnrollmentDTO.EnrollmentBuilder()
                    .setIDStudent(fieldIdStudent.getText())
                    .setIDCourse(comboBoxNRC.getValue().getNRC())
                    .build();

            ENROLLMENT_DAO.createOne(enrollmentDTO);
            Modal.displaySuccess("El académico ha sido registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Modal.displayError("No ha sido posible registrar académico debido a un error en el sistema.");
        }
    }

}