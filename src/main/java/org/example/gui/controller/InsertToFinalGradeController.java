package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dto.StudentDTO;
import org.example.db.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class InsertToFinalGradeController extends InsertIdController {
    private Stage reviewStage;

    @FXML
    private TextField idStudentTextField;

    public void setReviewStage(Stage reviewStage) {
        this.reviewStage = reviewStage;
    }

    @Override
    public void searchId(ActionEvent event) {
        String studentId = idStudentTextField.getText().trim();

        try {
            if (studentId.isEmpty()) {
                AlertDialog.showError("Por favor, ingrese un ID de estudiante.");
                return;
            }

            StudentDAO studentDAO = new StudentDAO();
            StudentDTO student = studentDAO.getOne(studentId);

            if (student == null) {
                AlertDialog.showError("No se encontró un estudiante con ese ID.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/example/RegisterFinalGradePage.fxml"
            )));
            Parent finalGradeView = loader.load();

            RegisterFinalGradeController finalGradeController = loader.getController();
            finalGradeController.setStudent(student);

            Scene finalGradeScene = new Scene(finalGradeView);
            reviewStage.setScene(finalGradeScene);
            reviewStage.show();

            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            AlertDialog.showError("Se produjo un error, intente más tarde.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
