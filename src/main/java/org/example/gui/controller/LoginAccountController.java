package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;

public class LoginAccountController {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        AccountDAO accountDAO = new AccountDAO();
        AcademicDAO academicDAO = new AcademicDAO();
        StudentDAO studentDAO = new StudentDAO();

        try {
            AccountDTO account = accountDAO.getOne(email);

            if (account != null && account.password().equals(password)) {
                AcademicDTO academic = academicDAO.getAll().stream()
                        .filter(academicDTO -> academicDTO.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);

                if (academic != null) {
                    Session.startSession(academic);
                    String role = academic.getRole();
                    switch (role.trim().toLowerCase()) {
                        case "professor":
                            openMainPage("/org/example/AcademicMainPage.fxml");
                            break;
                        case "evaluator":
                            openMainPage("/org/example/EvaluatorMainPage.fxml");
                            break;
                        default:
                            Modal.displayError("Tipo de usuario no encontrado");
                            break;
                    }
                    return;
                }

                StudentDTO student = studentDAO.getAll().stream()
                        .filter(studentDTO -> studentDTO.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);

                if (student != null) {
                    Session.startSession(student);
                    openMainPage("/org/example/StudentMainPage.fxml");
                    return;
                }

                Modal.displayError("La cuanta a la que intenta acceder no es estudiante ni academico");
            } else {
                Modal.displayError("Usuario y/o contraseña inexistentes");
            }
        } catch (SQLException e) {
            Modal.displayError("Ocurrio un error de conexión con la base de datos");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openMainPage(String vista) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
        Parent root = loader.load();
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
