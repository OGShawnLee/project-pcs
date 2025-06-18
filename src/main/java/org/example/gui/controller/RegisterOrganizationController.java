package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.dto.OrganizationDTO;
import org.example.business.dao.OrganizationDAO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class RegisterOrganizationController extends Controller {
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
  @FXML
  private TextField fieldPhoneNumber;

  public void handleRegister() {
    try {
      OrganizationDTO organizationDTO = new OrganizationDTO.OrganizationBuilder()
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setRepresentativeFullName(fieldRepresentative.getText())
        .setColony(fieldColony.getText())
        .setStreet(fieldStreet.getText())
        .setPhoneNumber(fieldPhoneNumber.getText())
        .build();

      OrganizationDTO existingOrganizationDTO = ORGANIZATION_DAO.getOne(organizationDTO.getEmail());

      if (existingOrganizationDTO != null) {
        Modal.displayError(
          "No ha sido posible registrar la organización debido a que ya existe una organización registrada con ese correo electrónico."
        );
        return;
      }

      ORGANIZATION_DAO.createOne(organizationDTO);
      Modal.displaySuccess("La organización ha sido registrada exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible registrar organización debido a un error en el sistema.");
    }
  }
}
