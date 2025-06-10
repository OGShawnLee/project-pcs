package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.Optional;

public class ManageStudentController extends ManageController<StudentDTO> {

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldPaternalLastName;
    @FXML
    private TextField fieldMaternalLastName;
    @FXML
    private TextField fieldEmail;
    @FXML
    private ComboBox<String> stateComboBox;

    private final AccountDAO ACCOUNT_DAO = new AccountDAO();
    private final StudentDAO STUDENT_DAO = new StudentDAO();

    @FXML
    public void initialize() {
        stateComboBox.getItems().addAll("Activo", "Archivado");
    }

    @Override
    protected void initialize(StudentDTO student) {
        super.initialize(student);
        loadStudentData(student);
    }

    private void loadStudentData(StudentDTO student) {
        fieldName.setText(student.getName());
        fieldPaternalLastName.setText(student.getPaternalLastName());
        fieldMaternalLastName.setText(student.getMaternalLastName());
        fieldEmail.setText(student.getEmail());

        String state = student.getState();
        if ("ACTIVE".equalsIgnoreCase(state)) {
            stateComboBox.setValue("Activo");
        } else if ("RETIRED".equalsIgnoreCase(state)) {
            stateComboBox.setValue("Archivado");
        }
    }


    @FXML
    @Override
    protected void handleUpdateCurrentDataObject() {
        try {
            String selectedState = stateComboBox.getValue();
            String internalState = selectedState.equalsIgnoreCase("Activo") ? "ACTIVE" : "RETIRED";

            StudentDTO previousStudent = getCurrentDataObject();

            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(previousStudent.getID())
                    .setName(fieldName.getText())
                    .setPaternalLastName(fieldPaternalLastName.getText())
                    .setMaternalLastName(fieldMaternalLastName.getText())
                    .setEmail(fieldEmail.getText())
                    .setState(internalState)
                    .setFinalGrade(previousStudent.getFinalGrade())
                    .build();

            AccountDTO existingAccount = ACCOUNT_DAO.getOne(updatedStudent.getEmail());
            if (existingAccount != null && !updatedStudent.getEmail().equals(previousStudent.getEmail())) {
                Modal.displayError("Ya existe una cuenta con ese correo electrónico.");
                return;
            }

            StudentDTO existingStudent = STUDENT_DAO.getOne(updatedStudent.getID());
            if (existingStudent != null && !existingStudent.getID().equals(previousStudent.getID())) {
                Modal.displayError("Ya existe un estudiante con la misma ID.");
                return;
            }

            ACCOUNT_DAO.updateOne(new AccountDTO(updatedStudent.getEmail(), updatedStudent.getID()));
            STUDENT_DAO.updateOne(updatedStudent);

            Modal.displaySuccess("Estudiante actualizado exitosamente.");
            navigateToStudentList();

        } catch (SQLException e) {
            Modal.displayError("Error en la conexión con la base de datos.");
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteStudent() {
        StudentDTO student = getCurrentDataObject();

        Optional<Boolean> confirmation = Modal.promptConfirmation(
                "Confirmar eliminación",
                "¿Deseas eliminar al estudiante?",
                "Esta acción eliminará permanentemente el estudiante, su cuenta y su inscripción (enrollment)."
        );

        if (confirmation.isEmpty() || !confirmation.get()) return;

        try {

            ACCOUNT_DAO.deleteOne(student.getEmail());

            STUDENT_DAO.deleteOne(student.getID());

            Modal.displaySuccess("Estudiante eliminado correctamente.");
            navigateToStudentList();

        } catch (SQLException e) {
            Modal.displayError("No se pudo eliminar el estudiante por un error en la base de datos.");
        }
    }



    public void navigateToStudentList() {
        ReviewStudentListController.navigateToStudentListPage(getScene());
    }
}