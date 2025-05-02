package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.db.dao.AccountDAO;
import org.example.db.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterStudentController {
    private final AccountDAO ACCOUNT_DAO = new AccountDAO();
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
      try{
        StudentDTO dataObjectStudent = new StudentDTO.StudentBuilder()
                .setPaternalLastName(fieldPaternalLastName.getText())
                .setMaternalLastName(fieldMaternalLastName.getText())
                .setName(fieldName.getText())
                .setID(fieldIdStudent.getText())
                .setEmail(fieldEmail.getText())
                .setFinalGrade("0")
                .build();

        AccountDTO existingAccount = ACCOUNT_DAO.getOne(dataObjectStudent.getEmail());
        if (existingAccount != null) {
            AlertDialog.showError("No ha sido posible registrar al estudiante debido a que ya existe una cuenta con ese correo electrónico.");
            return;
        }

        StudentDTO existingStudent = STUDENT_DAO.getOne(dataObjectStudent.getID());
        if (existingStudent != null) {
            AlertDialog.showError("No ha sido posible registrar al estudiante debido a que ya existe un estudiante con la misma ID de Trabajador.");
            return;
        }

        ACCOUNT_DAO.createOne(new AccountDTO(dataObjectStudent.getEmail(), dataObjectStudent.getID()));
        STUDENT_DAO.createOne(dataObjectStudent);
        AlertDialog.showSuccess("Estudiante registrado exitosamente.");
        returnToReviewStudentsPage(event);
      } catch (IllegalArgumentException e) {
        AlertDialog.showError(e.getMessage());
      } catch (SQLException e) {
        System.out.println(e.getMessage());
        AlertDialog.showError("No ha sido posible registrar al estudiante debido a un error de sistema.");
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
    }

    @FXML
    public void returnToReviewStudentsPage(ActionEvent event) throws IOException {
        Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/ReviewStudentsPage.fxml")));
        Scene newScene = new Scene(newView);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }
}