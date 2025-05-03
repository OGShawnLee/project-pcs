package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.business.dto.OrganizationDTO;
import org.example.db.dao.OrganizationDAO;
import org.example.gui.AlertDialog;
import org.example.gui.controller.ReviewOrganizationListController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterOrganizationController {
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private AnchorPane container;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldRepresentative;
  @FXML
  private TextField fieldColony;
  @FXML
  private TextField fieldStreet;

  public void handleRegisterOrganization(ActionEvent event) {
    try {
      OrganizationDTO dataObjectOrganization = new OrganizationDTO.OrganizationBuilder()
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setRepresentativeFullName(fieldRepresentative.getText())
        .setColony(fieldColony.getText())
        .setStreet(fieldStreet.getText())
        .build();

      OrganizationDTO dataObjectExistingOrganization = ORGANIZATION_DAO.getOne(dataObjectOrganization.getEmail());

      if (dataObjectExistingOrganization != null) {
        AlertDialog.showError(
          "No ha sido posible registrar la organización debido a que ya existe una organización registrada con ese correo electrónico."
        );
        return;
      }

      ORGANIZATION_DAO.createOne(dataObjectOrganization);
      AlertDialog.showSuccess("La organización ha sido registrada exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible registrar la organización debido a un error de sistema.");
    }
  }

  public void navigateToOrganizationList() {
    try {
      ReviewOrganizationListController.navigateToOrganizationListPage(
        (Stage) container.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de lista de organizaciones."
      );
    }
  }

  public static void navigateToRegisterOrganizationPage(Stage currentStage) throws IOException {
    Parent newView = FXMLLoader.load(Objects.requireNonNull(RegisterOrganizationController.class.getResource("/org/example/RegisterOrganizationPage.fxml")));
    Scene newScene = new Scene(newView);

    currentStage.setScene(newScene);
    currentStage.show();
  }
}
