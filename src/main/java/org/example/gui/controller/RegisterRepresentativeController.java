package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.RepresentativeDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class RegisterRepresentativeController extends Controller {
  private final RepresentativeDAO REPRESENTATIVE_DAO = new RepresentativeDAO();
  @FXML
  private TextField fieldEmail;
  @FXML
  private ComboBox<OrganizationDTO> fieldOrganizationEmail;
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
    ComboBoxLoader.loadComboBoxOrganization(fieldOrganizationEmail, false);
  }

  public void handleRegister() {
    try {
      RepresentativeDTO representativeDTO = new RepresentativeDTO.RepresentativeBuilder()
        .setEmail(fieldEmail.getText())
        .setOrganizationID(fieldOrganizationEmail.getValue().getEmail())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setPhoneNumber(fieldPhoneNumber.getText())
        .setPosition(fieldPosition.getText())
        .build();

      RepresentativeDTO existingRepresentative = REPRESENTATIVE_DAO.findOne(representativeDTO.getEmail());
      if (existingRepresentative != null) {
        Modal.displayError("No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador.");
        return;
      }

      REPRESENTATIVE_DAO.createOne(representativeDTO);
      Modal.displaySuccess("El académico ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      Modal.displayError("No ha sido posible registrar académico debido a un error en el sistema.");
    }
  }
}