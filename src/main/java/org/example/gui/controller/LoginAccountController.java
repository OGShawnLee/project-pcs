package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;
import org.example.gui.Session;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class LoginAccountController extends Controller {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    public void handleLogin() {
        String email = txtEmail.getText();
        String plainPassword = txtPassword.getText();

        AccountDAO accountDAO = new AccountDAO();
        AcademicDAO academicDAO = new AcademicDAO();
        StudentDAO studentDAO = new StudentDAO();

        try {
            AccountDTO account = accountDAO.getOne(email);

            if (account != null) {
                if (BCrypt.checkpw(plainPassword, account.password())) {
                    AcademicDTO academic = academicDAO.getAll().stream()
                            .filter(a -> a.getEmail().equals(email))
                            .findFirst()
                            .orElse(null);

                    if (academic != null) {
                        Session.startSession(academic);
                        String role = academic.getRole().trim().toLowerCase();

                        switch (role) {
                            case "professor" -> AcademicMainController.navigateToAcademicMain(getScene());
                            case "evaluator" -> EvaluatorMainController.navigateToEvaluatorMain(getScene());
                            default -> Modal.displayError("Tipo de usuario no encontrado");
                        }
                        return;
                    }

                    StudentDTO student = studentDAO.getAll().stream()
                            .filter(s -> s.getEmail().equals(email))
                            .findFirst()
                            .orElse(null);

                    if (student != null) {
                        Session.startSession(student);
                        StudentMainController.navigateToStudentMain(getScene());
                        return;
                    }

                    Modal.displayError("No se encontró un tipo de usuario asociado a este correo.");
                } else {
                    Modal.displayError("La contraseña de la cuenta es incorrecta");
                }
            } else {
                Modal.displayError("Usuario inexistente");
            }
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible iniciar sesión debido a un error de conexión con la base de datos.");
        }
    }
}
