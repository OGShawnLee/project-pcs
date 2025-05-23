package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class UpdateStudentController {

    @FXML
    private TextField paternalSurnameField;
    @FXML
    private TextField maternalSurnameField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;

    private StudentDTO loggedStudent;

    public void initialize() {
        Object user = Session.getCurrentUser();

        if (user instanceof StudentDTO student) {
            this.loggedStudent = student;
            paternalSurnameField.setText(student.getPaternalLastName());
            maternalSurnameField.setText(student.getMaternalLastName());
            nameField.setText(student.getName());
        } else {
            Modal.displayError("Usuario en sesión no es un estudiante válido.");
        }
    }

    @FXML
    public void updateStudentData(ActionEvent event) {
        if (loggedStudent == null) return;

        try {
            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(loggedStudent.getID())
                    .setEmail(loggedStudent.getEmail())
                    .setName(nameField.getText())
                    .setPaternalLastName(paternalSurnameField.getText())
                    .setMaternalLastName(maternalSurnameField.getText())
                    .setState(loggedStudent.getState())
                    .setCreatedAt(loggedStudent.getCreatedAt())
                    .setFinalGrade(loggedStudent.getFinalGrade())
                    .build();

            new StudentDAO().updateOne(updatedStudent);

            String newPassword = passwordField.getText();
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            AccountDTO updatedAccount = new AccountDTO(updatedStudent.getEmail(), hashedPassword);
            new AccountDAO().updateOne(updatedAccount);

            Session.startSession(updatedStudent);
            loggedStudent = updatedStudent;

            Modal.displaySuccess("Datos y contraseña actualizados correctamente");
            returnToMainPage(event);

        } catch (IllegalArgumentException e) {
            Modal.displayError("Error de validación");
        } catch (SQLException e) {
            Modal.displayError("Error al actualizar en base de datos");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void returnToMainPage(ActionEvent event) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/example/StudentMainPage.fxml"));
        javafx.scene.Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root));
        stage.setTitle("Menu Principal");
        stage.show();
    }
}
