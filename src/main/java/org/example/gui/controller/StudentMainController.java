package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.business.dao.PracticeDAO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;

import java.io.IOException;

public class StudentMainController {

    @FXML
    private void handleUpdateStudentAction(ActionEvent event) throws IOException {
        changeScene("/org/example/UpdateStudentPage.fxml", event);
    }

    @FXML
    private void handleReviewStudentProjectAction(ActionEvent event) throws IOException {
        try {
            StudentDTO student = (StudentDTO) Session.getCurrentUser();
            PracticeDAO practiceDAO = new PracticeDAO();

            boolean hasProject = practiceDAO.getAll().stream()
                    .anyMatch(practice -> practice.getIDStudent().equals(student.getID()));

            if (hasProject) {
                changeScene("/org/example/ReviewStudentProjectPage.fxml", event);
            } else {
                Modal.displayError("Actualmente no tienes un proyecto asignado.");
            }

        } catch (Exception e) {
            Modal.displayError("Ocurrió un error al verificar tu proyecto.");
        }
    }

    @FXML
    private void handleRegisterSelfEvaluationAction(ActionEvent event) throws IOException {
        changeScene("/org/example/RegisterSelfEvaluationPage.fxml", event);
    }

    @FXML
    private void handleRegisterProjectRequestAction(ActionEvent event) throws IOException {
        changeScene("/org/example/RegisterProjectRequestPage.fxml", event);
    }

    @FXML
    private void handleRegisterMonthlyReportAction(ActionEvent event) throws IOException {
        changeScene("/org/example/RegisterMonthlyReportPage.fxml", event);
    }

    private void changeScene(String path, ActionEvent event) throws IOException {
        Parent newSceneParent = FXMLLoader.load(getClass().getResource(path));
        Scene newScene = new Scene(newSceneParent);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }


}