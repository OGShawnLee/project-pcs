package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterProjectController {
  private final ProjectDAO PROJECT_DAO = new ProjectDAO();
  @FXML
  private AnchorPane container;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldMethodology;
  @FXML
  private TextField fieldSector;

  public void handleRegisterProject(ActionEvent event) {
    try {
      ProjectDTO dataObjectProject = new ProjectDTO.ProjectBuilder()
        .setIDOrganization(fieldEmail.getText())
        .setName(fieldName.getText())
        .setMethodology(fieldMethodology.getText())
        .setSector(fieldSector.getText())
        .build();

      PROJECT_DAO.createOne(dataObjectProject);
      AlertDialog.showSuccess("El proyecto ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible registrar el proyecto debido a un error de sistema.");
    }
  }

  public void navigateToProjectList() {
    try {
      ReviewProjectListController.navigateToProjectListPage(
        (Stage) container.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de lista de académicos."
      );
    }
  }

  public static void navigateToRegisterProjectPage(Stage currentStage) throws IOException {
    Parent newView = FXMLLoader.load(Objects.requireNonNull(RegisterProjectController.class.getResource("/org/example/RegisterProjectPage.fxml")));
    Scene newScene = new Scene(newView);

    currentStage.setScene(newScene);
    currentStage.show();
  }
}
