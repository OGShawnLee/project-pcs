package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.dto.OrganizationDTO;
import org.example.business.Result;
import org.example.business.validation.Validator;
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
    OrganizationDTO organization = new OrganizationDTO.OrganizationBuilder()
      .setEmail(fieldEmail.getText())
      .setName(fieldName.getText())
      .setRepresentativeFullName(fieldRepresentative.getText())
      .setColony(fieldColony.getText())
      .setStreet(fieldStreet.getText())
      .build();

    Result<String> nameResult = Validator.getFilledString(
      organization.getName(),
      "Nombre no puede estar vacío",
      128
    );

    if (nameResult.isFailure()) {
      AlertDialog.showError(nameResult.getError());
      return;
    }

    Result<String> emailResult = Validator.getEmail(
      organization.getEmail(),
      "Correo electrónico no es valido",
      128
    );

    if (emailResult.isFailure()) {
      AlertDialog.showError(emailResult.getError());
      return;
    }

    Result<String> representativeResult = Validator.getFilledString(
      organization.getRepresentativeFullName(),
      "Nombre del representante no puede estar vacío",
      128
    );

    if (representativeResult.isFailure()) {
      AlertDialog.showError(representativeResult.getError());
      return;
    }

    Result<String> colonyResult = Validator.getFilledString(
      organization.getColony(),
      "Colonia no puede estar vacía",
      128
    );

    if (colonyResult.isFailure()) {
      AlertDialog.showError(colonyResult.getError());
      return;
    }

    Result<String> streetResult = Validator.getFilledString(
      organization.getStreet(),
      "Calle no puede estar vacía",
      128
    );

    if (streetResult.isFailure()) {
      AlertDialog.showError(streetResult.getError());
      return;
    }

    try {
      OrganizationDTO existingOrganization = ORGANIZATION_DAO.getOne(organization.getEmail());

      if (existingOrganization != null) {
        AlertDialog.showError(
          "No ha sido posible registrar la organización debido a que ya existe una registrada con ese correo electrónico."
        );
        return;
      }

      ORGANIZATION_DAO.createOne(organization);
      AlertDialog.showSuccess("La organización ha sido registrada exitosamente.");
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible registrar la organización debido a un error de sistema.");
    }
  }
}
