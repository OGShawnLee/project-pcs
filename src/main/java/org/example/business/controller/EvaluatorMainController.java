package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EvaluatorMainController {
    @FXML
    private void handleRegisterEvaluationAction(ActionEvent event) throws IOException {
        Parent newSceneParent = FXMLLoader.load(getClass().getResource("/org/example/RegisterEvaluationPage.fxml"));
        Scene newScene = new Scene(newSceneParent);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }

    @FXML
    private void handleReviewEvaluationAction(ActionEvent event) throws IOException {
        Parent newSceneParent = FXMLLoader.load(getClass().getResource("/org/example/ReviewEvaluationPage.fxml"));
        Scene newScene = new Scene(newSceneParent);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }
}
