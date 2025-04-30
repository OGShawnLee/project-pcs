package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.Result;
import org.example.business.dto.StudentDTO;
import org.example.business.validation.Validator;
import org.example.db.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ManageStudentController {

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField paternalLastNameTextField;

    @FXML
    private TextField maternalLastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> stateComboBox;

    @FXML
    private Button updateStudent;

    private StudentDTO currentStudent;

    @FXML
    public void initialize() {
        stateComboBox.getItems().addAll("Activo", "Archivado");
    }

    public void setStudent(StudentDTO student) {
        this.currentStudent = student;

        idTextField.setText(student.getID());
        nameTextField.setText(student.getName());
        paternalLastNameTextField.setText(student.getPaternalLastName());
        maternalLastNameTextField.setText(student.getMaternalLastName());
        emailTextField.setText(student.getEmail());

        String state = student.getState();
        if (state.equalsIgnoreCase("ACTIVE")) {
            stateComboBox.setValue("Activo");
        } else if (state.equalsIgnoreCase("RETIRED")) {
            stateComboBox.setValue("Archivado");
        }
    }

    @FXML
    public void updateStudentData(ActionEvent event) throws IOException {
        try {
            String selectedState = stateComboBox.getValue();
            String finalState = selectedState.equals("Activo") ? "ACTIVE" : "RETIRED";

            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(currentStudent.getID())
                    .setName(nameTextField.getText())
                    .setPaternalLastName(paternalLastNameTextField.getText())
                    .setMaternalLastName(maternalLastNameTextField.getText())
                    .setEmail(emailTextField.getText())
                    .setState(finalState)
                    .build();

            Result<String> nameResult = Validator.getFilledString(
                    updatedStudent.getName(),
                    "Nombre no puede estar vacío"
            );

            if (nameResult.isFailure()) {
                AlertDialog.showError(nameResult.getError());
                return;
            }

            Result<String> nameLettersResult = Validator.getWords(
                    updatedStudent.getName(),
                    "El nombre tiene caracteres no validos"
            );

            if (nameLettersResult.isFailure()) {
                AlertDialog.showError(nameLettersResult.getError());
                return;
            }

            Result<String> nameLenghtResult = Validator.getMaxLenght(
                    updatedStudent.getName(),
                    "El nombre excede los caracteres permitidos",
                    127
            );
            if (nameLenghtResult.isFailure()) {
                AlertDialog.showError(nameLenghtResult.getError());
                return;
            }

            Result<String> emailResult = Validator.getEmail(
                    updatedStudent.getEmail(),
                    "El correo electrónico no es valido"
            );

            if (emailResult.isFailure()) {
                AlertDialog.showError(emailResult.getError());
                return;
            }

            Result<String> paternalLastNameNullResult = Validator.getFilledString(
                    updatedStudent.getPaternalLastName(),
                    "El apellido paterno no puede estar vacio"
            );

            if (paternalLastNameNullResult.isFailure()) {
                AlertDialog.showError(paternalLastNameNullResult.getError());
                return;
            }

            Result<String> paternalLastNameLettersResult = Validator.getWords(
                    updatedStudent.getName(),
                    "El apellido paterno tiene caracteres no validos"
            );

            if (paternalLastNameLettersResult.isFailure()) {
                AlertDialog.showError(paternalLastNameLettersResult.getError());
                return;
            }

            Result<String> paternalLastNameLenghtResult = Validator.getMaxLenght(
                    updatedStudent.getMaternalLastName(),
                    "El apellido paterno excede los caracteres permitidos",
                    127
            );
            if (paternalLastNameLenghtResult.isFailure()) {
                AlertDialog.showError(paternalLastNameLenghtResult.getError());
                return;
            }

            Result<String> maternalLastNameNullResult = Validator.getFilledString(
                    updatedStudent.getMaternalLastName(),
                    "El apellido materno no puede estar vacio"
            );

            if (maternalLastNameNullResult.isFailure()) {
                AlertDialog.showError(maternalLastNameNullResult.getError());
                return;
            }

            Result<String> maternalLastNameLettersResult = Validator.getWords(
                    updatedStudent.getName(),
                    "El apellido materno tiene caracteres no validos"
            );

            if (maternalLastNameLettersResult.isFailure()) {
                AlertDialog.showError(maternalLastNameLettersResult.getError());
                return;
            }

            Result<String> maternalLastNameLenghtResult = Validator.getMaxLenght(
                    updatedStudent.getMaternalLastName(),
                    "El apellido materno exede los caracteres permitidos",
                    127
            );

            if (maternalLastNameLenghtResult.isFailure()) {
                AlertDialog.showError(maternalLastNameLenghtResult.getError());
                return;
            }
            Result<String> studentIdResult = Validator.getFilledString(
                    updatedStudent.getID(),
                    "La matricula no puede estar vacía"
            );

            if (studentIdResult.isFailure()) {
                AlertDialog.showError(studentIdResult.getError());
                return;
            }

            Result<String> studentIdCharResult = Validator.getIDStudent(
                    updatedStudent.getID(),
                    "La matricula no es valida para su registro",
                    8
            );

            if (studentIdCharResult.isFailure()) {
                AlertDialog.showError(studentIdCharResult.getError());
                return;
            }

            StudentDAO studentDAO = new StudentDAO();
            studentDAO.updateOne(updatedStudent);

            AlertDialog.showSuccess("Datos actualizados correctamente.");

            Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/ReviewStudentsPage.fxml")));
            Scene newScene = new Scene(newView);
            Stage stage = (Stage) updateStudent.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();

        } catch (SQLException e){
            AlertDialog.showError("No se pudieron actualizar los datos.");

            Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/ReviewStudentsPage.fxml")));
            Scene newScene = new Scene(newView);
            Stage stage = (Stage) updateStudent.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        }
    }
}
