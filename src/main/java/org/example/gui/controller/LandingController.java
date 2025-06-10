package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.example.business.auth.AuthClient;

public abstract class LandingController extends Controller {
  @FXML
  private Label labelEmail;

  public void initialize() {
    labelEmail.setText(AuthClient.getInstance().getCurrentUser().email());
  }

  public void handleLogOut() {
    AuthClient.getInstance().setCurrentUser(null);
    navigateFromThisPageTo("Página de Inicio", "LoginPage");
  }
}
