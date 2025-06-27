package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConfirmationController {
  @FXML
  private Text textDetails;
  private Stage stage;
  private boolean isConfirmed;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setMessage(String message) {
    textDetails.setText(message);
  }

  public boolean getIsConfirmed() {
    return isConfirmed;
  }

  private void setIsConfirmed(boolean isConfirmed) {
    this.isConfirmed = isConfirmed;
  }

  @FXML
  private void handleConfirm() {
    setIsConfirmed(true);
    stage.close();
  }

  @FXML
  private void handleCancel() {
    setIsConfirmed(false);
    stage.close();
  }
}