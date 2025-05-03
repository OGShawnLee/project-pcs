package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterAcademicController {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  private final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  @FXML
  private AnchorPane container;
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
  private ComboBox<String> fieldRole;

  @FXML
  public void initialize() {
    fieldRole.getItems().addAll("Evaluador", "Evaluador y Profesor", "Profesor");
    fieldRole.setValue("Profesor");
  }

  public void handleRegisterAcademic(ActionEvent event) {
    try {
      AcademicDTO dataObjectAcademic = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(fieldRole.getValue())
        .build();

      AccountDTO existingAccount = ACCOUNT_DAO.getOne(dataObjectAcademic.getEmail());
      if (existingAccount != null) {
        AlertDialog.showError("No ha sido posible registrar el académico debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      AcademicDTO existingAcademic = ACADEMIC_DAO.getOne(dataObjectAcademic.getID());
      if (existingAcademic != null) {
        AlertDialog.showError("No ha sido posible registrar el académico debido a que ya existe un académico con la misma ID de Trabajador.");
        return;
      }

      ACCOUNT_DAO.createOne(new AccountDTO(dataObjectAcademic.getEmail(), dataObjectAcademic.getID()));
      ACADEMIC_DAO.createOne(dataObjectAcademic);
      AlertDialog.showSuccess("El académico ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      AlertDialog.showError("No ha sido posible registrar el académico debido a un error de sistema.");
    }
  }

  public void navigateToAcademicList() {
    try {
      ReviewAcademicListController.navigateToAcademicListPage(
        (Stage) container.getScene().getWindow()
      );
    } catch (IOException e) {
      AlertDialog.showError(
        "No ha sido posible navegar a página de lista de académicos."
      );
    }
  }

  public static void navigateToRegisterAcademicPage(Stage currentStage) throws IOException {
    Parent newView = FXMLLoader.load(Objects.requireNonNull(RegisterAcademicController.class.getResource("/org/example/RegisterAcademicPage.fxml")));
    Scene newScene = new Scene(newView);

    currentStage.setScene(newScene);
    currentStage.show();
  }
}