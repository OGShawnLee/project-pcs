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
import org.example.gui.combobox.AcademicComboBoxLoader;

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
  private ComboBox<AcademicRole> comboBoxAcademicRole;

  public void initialize() {
    AcademicComboBoxLoader.loadAcademicRoleComboBox(comboBoxAcademicRole);
  }

  public AcademicDTO createAcademicDTOFromFields() {
    return new AcademicDTO.AcademicBuilder()
      .setID(fieldIDAcademic.getText())
      .setEmail(fieldEmail.getText())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setRole(comboBoxAcademicRole.getValue())
      .build();
  }

  /**
   * Verifies if an academic account with the given email already exists and shows an error.
   *
   * @param academicDTO The AcademicDTO containing the email to check.
   * @return true if no duplicate account exists, false otherwise.
   * @throws UserDisplayableException if an error occurs while checking for duplicates.
   */
  public boolean verifyDuplicateAccount(AcademicDTO academicDTO) throws UserDisplayableException {
    AccountDTO existingAccountDTO = ACCOUNT_DAO.getOne(academicDTO.getEmail());

    if (existingAccountDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar académico debido a que ya existe una cuenta con ese correo electrónico.");
      return false;
    }

    return true;
  }

  /**
   * Verifies if an academic with the given ID already exists and shows an error.
   *
   * @param academicDTO The AcademicDTO containing the ID to check.
   * @return true if no duplicate academic exists, false otherwise.
   * @throws UserDisplayableException if an error occurs while checking for duplicates.
   */
  public boolean verifyDuplicateAcademic(AcademicDTO academicDTO) throws UserDisplayableException {
    AcademicDTO existingAcademicDTO = ACADEMIC_DAO.getOne(academicDTO.getID());

    if (existingAcademicDTO != null) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador."
      );
      return false;
    }

    return true;
  }

  public void updateAcademicDTO(AcademicDTO academicDTO) throws UserDisplayableException {
    ACADEMIC_DAO.createOne(academicDTO);
    AlertFacade.showSuccessAndWait("El académico ha sido registrado exitosamente.");
  }

  public void handleRegister() {
    try {
      AcademicDTO academicDTO = createAcademicDTOFromFields();
      if (verifyDuplicateAccount(academicDTO) && verifyDuplicateAcademic(academicDTO)) {
        updateAcademicDTO(academicDTO);
      }
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar académico.", e.getMessage());
    }
  }
}