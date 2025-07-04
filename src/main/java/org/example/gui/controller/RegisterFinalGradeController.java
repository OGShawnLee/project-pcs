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
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.io.IOException;
import java.util.Objects;

// TODO: UPDATE USE CASE
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
      AlertFacade.showErrorAndWait("El estudiante no está disponible.");
    }
  }

  @FXML
  public void updateStudentFinalGrade(ActionEvent event) throws IOException {
    if (currentStudent == null) {
      AlertFacade.showErrorAndWait("No se ha seleccionado un estudiante.");
      return;
    }
    try {
      int newFinalGrade = Integer.parseInt(finalGradeField.getText());

      if (newFinalGrade < 0 || newFinalGrade > 10) {
        AlertFacade.showErrorAndWait("La calificación final debe estar entre 0 y 10.");
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

      AlertFacade.showSuccessAndWait("La calificación fue asignada correctamente.");
      goToReviewStudentsPage();

    } catch (NumberFormatException e) {
      AlertFacade.showErrorAndWait("La calificación ingresada no es válida.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
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
