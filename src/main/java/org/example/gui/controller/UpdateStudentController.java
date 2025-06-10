package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class UpdateStudentController extends ManageController<StudentDTO> {

    @FXML
    private TextField paternalSurnameField;
    @FXML
    private TextField maternalSurnameField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;

    private StudentDTO loggedStudent;

    @Override
    @FXML
    protected void initialize(StudentDTO student) {
        super.initialize(student);
        this.loggedStudent = student;
        loadStudentData(student);
    }

    private void loadStudentData(StudentDTO student) {
        paternalSurnameField.setText(student.getPaternalLastName());
        maternalSurnameField.setText(student.getMaternalLastName());
        nameField.setText(student.getName());
    }

    @Override
    @FXML
    protected void handleUpdateCurrentDataObject() {
        if (loggedStudent == null) {
            Modal.displayError("No se pudo recuperar la información del estudiante.");
            return;
        }

        String name = nameField.getText().trim();
        String paternal = paternalSurnameField.getText().trim();
        String maternal = maternalSurnameField.getText().trim();
        String newPassword = passwordField.getText();

        if (name.isEmpty() || paternal.isEmpty() || maternal.isEmpty()) {
            Modal.displayError("Todos los campos deben estar completos.");
            return;
        }

        try {
            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(loggedStudent.getID())
                    .setEmail(loggedStudent.getEmail())
                    .setName(name)
                    .setPaternalLastName(paternal)
                    .setMaternalLastName(maternal)
                    .setState(loggedStudent.getState())
                    .setCreatedAt(loggedStudent.getCreatedAt())
                    .setFinalGrade(loggedStudent.getFinalGrade())
                    .build();

            new StudentDAO().updateOne(updatedStudent);

            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (newPassword.length() < 6) {
                    Modal.displayError("La contraseña debe tener al menos 6 caracteres.");
                    return;
                }

                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                AccountDTO updatedAccount = new AccountDTO(updatedStudent.getEmail(), hashedPassword);
                new AccountDAO().updateOne(updatedAccount);
            }

            Modal.displaySuccess("Tu perfil ha sido actualizado correctamente.");
            StudentMainController.navigateToStudentMain(getScene());

        } catch (IllegalArgumentException e) {
            Modal.displayError("Error de validación: " + e.getMessage());
        } catch (SQLException e) {
            Modal.displayError("Error al actualizar la base de datos.");
        }
    }

    public void navigateToStudentMain() {
        StudentMainController.navigateToStudentMain(getScene());
    }
}
