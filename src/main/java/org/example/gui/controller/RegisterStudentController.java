package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.EnrollmentDTO;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.CourseDAO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterStudentController extends Controller{
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
    private TextField fieldIdStudent;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldNRC;



    public void handleRegisterStudent() {
        try{
            StudentDTO dataObjectStudent = new StudentDTO.StudentBuilder()
                    .setPaternalLastName(fieldPaternalLastName.getText())
                    .setMaternalLastName(fieldMaternalLastName.getText())
                    .setName(fieldName.getText())
                    .setID(fieldIdStudent.getText())
                    .setEmail(fieldEmail.getText())
                    .setFinalGrade(0)
                    .build();

            AccountDTO existingAccount = ACCOUNT_DAO.getOne(dataObjectStudent.getEmail());
            if (existingAccount != null) {
                Modal.displayError("No ha sido posible registrar al estudiante debido a que ya existe una cuenta con ese correo electrónico.");
                return;
            }

            StudentDTO existingStudent = STUDENT_DAO.getOne(dataObjectStudent.getID());
            if (existingStudent != null) {
                Modal.displayError("No ha sido posible registrar al estudiante debido a que ya existe un estudiante con la misma ID de Trabajador.");
                return;
            }

            String nrc = fieldNRC.getText().trim();
            if (nrc.isEmpty()) {
                Modal.displayError("Debe ingresar un NRC.");
                return;
            }

            CourseDAO courseDAO = new CourseDAO();
            CourseDTO course = courseDAO.getOne(nrc);
            if (course == null) {
                Modal.displayError("El NRC ingresado no existe en el sistema.");
                return;
            }

            String hashedPassword = BCrypt.hashpw(dataObjectStudent.getID(), BCrypt.gensalt());
            ACCOUNT_DAO.createOne(new AccountDTO(dataObjectStudent.getEmail(), hashedPassword));
            STUDENT_DAO.createOne(dataObjectStudent);
            EnrollmentDTO enrollment = new EnrollmentDTO.EnrollmentBuilder()
                    .setIDCourse(nrc)
                    .setIDStudent(dataObjectStudent.getID())
                    .build();
            ENROLLMENT_DAO.createOne(enrollment);
            Modal.displaySuccess("Estudiante registrado exitosamente.");
            ReviewStudentListController.navigateToStudentListPage(getScene());
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Modal.displayError("No ha sido posible registrar al estudiante debido a un error de sistema.");
        }
    }

    public void goBack() {
        ReviewStudentListController.navigateToStudentListPage(getScene());
    }

    public static void navigateToRegisterStudentPage(Stage currentStage) {
        navigateTo(currentStage, "Registrar estudiante", "RegisterStudentPage");
    }
}
