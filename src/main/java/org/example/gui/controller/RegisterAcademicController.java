package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.auth.AuthClient;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.gui.Modal;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

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
  private ComboBox<AcademicDTO.Role> fieldRole;

  public void initialize() {
    loadRoleComboBox(fieldRole);
  }

  public static void loadRoleComboBox(ComboBox<AcademicDTO.Role> fieldRole) {
    fieldRole.getItems().addAll(AcademicDTO.Role.values());
    fieldRole.setValue(AcademicDTO.Role.ACADEMIC);
  }

  public void handleRegister() {
    try {
      AcademicDTO academicDTO = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(fieldRole.getValue())
        .build();

      AccountDTO existingAccountDTO = ACCOUNT_DAO.getOne(academicDTO.getEmail());
      if (existingAccountDTO != null) {
        Modal.displayError("No ha sido posible registrar académico debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      AcademicDTO existingAcademicDTO = ACADEMIC_DAO.getOne(academicDTO.getID());
      if (existingAcademicDTO != null) {
        Modal.displayError("No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador.");
        return;
      }

      ACADEMIC_DAO.createOne(academicDTO);
      Modal.displaySuccess("El académico ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      Modal.displayError("No ha sido posible registrar académico debido a un error en el sistema.");
    }
  }
}