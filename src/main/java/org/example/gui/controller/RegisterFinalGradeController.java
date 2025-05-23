package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterFinalGradeController {

    @FXML
    private TextField finalGradeField;
    @FXML
    private Button registerFinalGradeButton;

    private StudentDTO currentStudent;

    public void setStudent(StudentDTO student) {
        this.currentStudent = student;
        if (currentStudent != null) {
            finalGradeField.setText(String.valueOf(student.getFinalGrade()));  // Establece la calificación actual
        } else {
            Modal.displayError("El estudiante no está disponible.");
        }
    }

    @FXML
    public void updateStudentFinalGrade(ActionEvent event) throws IOException {
        if (currentStudent == null) {
            Modal.displayError("No se ha seleccionado un estudiante.");
            return;
        }
        try {
            int newFinalGrade = Integer.parseInt(finalGradeField.getText());

            if (newFinalGrade < 0 || newFinalGrade > 10) {
                Modal.displayError("La calificación final debe estar entre 0 y 10.");
                return;
            }

            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(currentStudent.getID())
                    .setName(currentStudent.getName())
                    .setPaternalLastName(currentStudent.getPaternalLastName())
                    .setMaternalLastName(currentStudent.getMaternalLastName())
                    .setEmail(currentStudent.getEmail())
                    .setState(currentStudent.getState())
                    .setFinalGrade(newFinalGrade)
                    .build();

            StudentDAO studentDAO = new StudentDAO();
            studentDAO.updateOne(updatedStudent);

            Modal.displaySuccess("La calificación fue asignada correctamente.");
            goToReviewStudentsPage();

        } catch (NumberFormatException e) {
            Modal.displayError("La calificación ingresada no es válida.");
        } catch (SQLException e){
            Modal.displayError("No se pudieron actualizar los datos.");
        }
    }

    private void goToReviewStudentsPage() throws IOException {
        Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/ReviewStudentListPage.fxml")));
        Scene newScene = new Scene(newView);
        Stage stage = (Stage) registerFinalGradeButton.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }
}
