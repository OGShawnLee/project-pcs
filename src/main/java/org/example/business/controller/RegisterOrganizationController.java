package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.dto.OrganizationDTO;
import org.example.db.dao.OrganizationDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class RegisterOrganizationController {
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
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
}
