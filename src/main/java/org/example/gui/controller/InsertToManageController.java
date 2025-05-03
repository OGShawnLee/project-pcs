package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.AlertDialog;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class InsertToManageController extends InsertIdController {
    private Stage reviewStage;

    @FXML
    private TextField idStudentTextField;

    public void setReviewStage(Stage reviewStage) {
        this.reviewStage = reviewStage;
    }

    @Override
    public void searchId(ActionEvent event) {
        String idStudent = idStudentTextField.getText();
        try {
            StudentDAO dao = new StudentDAO();
            StudentDTO student = dao.getOne(idStudent);

            if (student == null) {
                AlertDialog.showError("Estudiante no encontrado");
                Parent gestionView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                        "/org/example/ReviewStudentsPage.fxml"
                )));
                Scene gestionScene = new Scene(gestionView);
                reviewStage.setScene(gestionScene);
                reviewStage.show();
                if (stage != null) {
                    stage.close();
                }
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/org/example/ManageStudentPage.fxml"
            ));
            Parent ManageView = loader.load();

            ManageStudentController controller = loader.getController();
            controller.setStudent(student);

            Scene gestionScene = new Scene(ManageView);
            reviewStage.setScene(gestionScene);
            reviewStage.show();

            if (stage != null) {
                stage.close();
            }

        } catch (SQLException | IOException e) {
            AlertDialog.showError("No se pudo cargar la gestion debido a un error del sistema");
        }
    }
}
