package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.util.Objects;

public class InsertToFinalGradeController extends InsertIdController{
    private Stage reviewStage;

    public void setReviewStage(Stage reviewStage) {
        this.reviewStage = reviewStage;
    }

    @Override
    public void searchId(javafx.event.ActionEvent event) {
        try {
            Parent finalGradeView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/org/example/RegisterFinalGradePage.fxml"
            )));
            Scene finalGradeScene = new Scene(finalGradeView);
            reviewStage.setScene(finalGradeScene);
            reviewStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            AlertDialog.showError("Se produjo un error, intente mas tarde");
        }
    }
}
