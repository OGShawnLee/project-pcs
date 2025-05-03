package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class InsertIdController {
    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public abstract void searchId(javafx.event.ActionEvent event) throws IOException;
}
