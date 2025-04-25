package org.example.business.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class InsertIdController {
    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public abstract void searchId(javafx.event.ActionEvent event);
}
