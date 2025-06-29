package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.RepresentativeDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class RegisterRepresentativeController extends Controller {
  private final RepresentativeDAO REPRESENTATIVE_DAO = new RepresentativeDAO();
  @FXML
  private TextField fieldEmail;
  @FXML
  private ComboBox<OrganizationDTO> comboBoxOrganization;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private TextField fieldPhoneNumber;
  @FXML
  private TextField fieldPosition;

  public void initialize() {
    ComboBoxLoader.loadComboBoxOrganization(comboBoxOrganization);
  }

  public RepresentativeDTO getDTOFromFields() {
    return new RepresentativeDTO.RepresentativeBuilder()
      .setEmail(fieldEmail.getText())
      .setOrganizationID(comboBoxOrganization.getValue().getEmail())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setPosition(fieldPosition.getText())
      .build();
  }

  public void handleRegister() {
    try {
      RepresentativeDTO representativeDTO = getDTOFromFields();

      RepresentativeDTO existingRepresentative = REPRESENTATIVE_DAO.findOne(representativeDTO.getEmail());
      if (existingRepresentative != null) {
        AlertFacade.showErrorAndWait("No ha sido posible registrar representante debido a que ya existe un representante con el mismo correo electrónico.");
        return;
      }

      REPRESENTATIVE_DAO.createOne(representativeDTO);
      AlertFacade.showSuccessAndWait("El representante ha sido registrado exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}