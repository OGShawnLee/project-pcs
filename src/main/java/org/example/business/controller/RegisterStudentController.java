package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.Result;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.validation.Validator;
import org.example.db.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class RegisterStudentController {
    private final StudentDAO STUDENT_DAO = new StudentDAO();
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

    public void handleRegisterStudent(ActionEvent event) {
        StudentDTO student = new StudentDTO.StudentBuilder()
                .setPaternalLastName(fieldPaternalLastName.getText())
                .setMaternalLastName(fieldMaternalLastName.getText())
                .setName(fieldName.getText())
                .setID(fieldIdStudent.getText())
                .setEmail(fieldEmail.getText())
                .build();

        AccountDTO account = new AccountDTO(fieldEmail.getText(), fieldIdStudent.getText());

        Result<String> passwordResult = Validator.getFilledString(
                account.password(),
                "Password no puede estar vacío",
                128
        );

        if (passwordResult.isFailure()) {
            AlertDialog.showError(passwordResult.getError());
            return;
        }

        Result<String> nameResult = Validator.getFilledString(
                student.getName(),
                "Nombre no puede estar vacío",
                128
        );

        if (nameResult.isFailure()) {
            AlertDialog.showError(nameResult.getError());
            return;
        }

        Result<String> emailResult = Validator.getEmail(
                student.getEmail(),
                "Correo electrónico no es valido",
                128
        );

        if (emailResult.isFailure()) {
            AlertDialog.showError(emailResult.getError());
            return;
        }

        Result<String> paternalLastNameResult = Validator.getFilledString(
                student.getPaternalLastName(),
                "El apellido paterno no puede estar vacio",
                128
        );

        if (paternalLastNameResult.isFailure()) {
            AlertDialog.showError(paternalLastNameResult.getError());
            return;
        }

        Result<String> maternalLastNameResult = Validator.getFilledString(
                student.getMaternalLastName(),
                "El apellido materno no puede estar vacio",
                128
        );

        if (maternalLastNameResult.isFailure()) {
            AlertDialog.showError(maternalLastNameResult.getError());
            return;
        }

        Result<String> studentIdResult = Validator.getFilledString(
                student.getID(),
                "La matricula no puede estar vacía",
                128
        );

        if (studentIdResult.isFailure()) {
            AlertDialog.showError(studentIdResult.getError());
            return;
        }


        try {
            StudentDTO existingOrganization = STUDENT_DAO.getOne(student.getEmail());

            if (existingOrganization != null) {
                AlertDialog.showError(
                        "No ha sido posible registrar al estudiante debido a que ya existe un estudiante " +
                                "registrado con ese correo electronico."
                );
                return;
            }

            STUDENT_DAO.createOne(student);
            AlertDialog.showSuccess("El estudiante ha sido registrado exitosamente.");
        } catch (SQLException e) {
            AlertDialog.showError("No ha sido posible registrar al estudiante debido a un error de sistema.");
        }
    }

}