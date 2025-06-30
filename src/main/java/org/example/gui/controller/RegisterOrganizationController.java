package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.example.business.dto.OrganizationDTO;
import org.example.business.dao.OrganizationDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class RegisterOrganizationController extends Controller {
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldAddress;
  @FXML
  private TextField fieldPhoneNumber;

  private OrganizationDTO createOrganizationDTOFromFields() {
    return new OrganizationDTO.OrganizationBuilder()
      .setName(fieldName.getText())
      .setEmail(fieldEmail.getText())
      .setAddress(fieldAddress.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .build();
  }

  public void handleRegister() {
    try {
      OrganizationDTO organizationDTO = createOrganizationDTOFromFields();
      OrganizationDTO existingOrganizationDTO = ORGANIZATION_DAO.getOne(organizationDTO.getEmail());

      if (existingOrganizationDTO != null) {
        AlertFacade.showErrorAndWait(
          "No ha sido posible registrar la organización debido a que ya existe una organización registrada con ese correo electrónico."
        );
        return;
      }

      ORGANIZATION_DAO.createOne(organizationDTO);
      AlertFacade.showSuccessAndWait("La organización ha sido registrada exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
