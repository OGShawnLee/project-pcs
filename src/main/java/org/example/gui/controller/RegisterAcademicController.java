package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.enumeration.AcademicRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class RegisterAcademicController extends Controller {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  private final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  @FXML
  private TextField fieldIDAcademic;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private TextField fieldPhoneNumber;
  @FXML
  private ComboBox<AcademicRole> fieldRole;

  public void initialize() {
    loadRoleComboBox(fieldRole);
  }

  public static void loadRoleComboBox(ComboBox<AcademicRole> fieldRole) {
    fieldRole.getItems().addAll(AcademicRole.values());
    fieldRole.setValue(AcademicRole.ACADEMIC);
  }

  public void handleRegister() {
    try {
      AcademicDTO academicDTO = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setPhoneNumber(fieldPhoneNumber.getText())
        .setRole(fieldRole.getValue())
        .build();

      AccountDTO existingAccountDTO = ACCOUNT_DAO.getOne(academicDTO.getEmail());
      if (existingAccountDTO != null) {
        AlertFacade.showErrorAndWait("No ha sido posible registrar académico debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      AcademicDTO existingAcademicDTO = ACADEMIC_DAO.getOne(academicDTO.getID());
      if (existingAcademicDTO != null) {
        AlertFacade.showErrorAndWait("No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador.");
        return;
      }

      ACADEMIC_DAO.createOne(academicDTO);
      AlertFacade.showSuccessAndWait("El académico ha sido registrado exitosamente.");
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}