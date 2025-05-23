package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.EnrollmentDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.AccountDAO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dao.filter.FilterEnrollment;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ManageStudentController {

    private final AccountDAO ACCOUNT_DAO = new AccountDAO();
    private final StudentDAO STUDENT_DAO = new StudentDAO();

    @FXML
    private TextField fieldIDStudent;
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
    @FXML
    private TextField fieldNRC;

    private StudentDTO previousStudent;

    @FXML
    public void initialize() {
        stateComboBox.getItems().addAll("Activo", "Archivado");
    }

    public void setStudent(StudentDTO student) {
        this.previousStudent = student;

        fieldIDStudent.setText(student.getID());
        fieldName.setText(student.getName());
        fieldPaternalLastName.setText(student.getPaternalLastName());
        fieldMaternalLastName.setText(student.getMaternalLastName());
        fieldEmail.setText(student.getEmail());

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        try {
            var enrollment = enrollmentDAO.getOne(
                    new FilterEnrollment(previousStudent.getID(), null)
            );
            if (enrollment != null) {
                fieldNRC.setText(enrollment.getIDCourse());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Modal.displayError("No se pudo cargar el NRC del estudiante debido a un error de conexion con la base de datos");
        }

        String state = student.getState();
        if (state.equalsIgnoreCase("ACTIVE")) {
            stateComboBox.setValue("Activo");
        } else if (state.equalsIgnoreCase("RETIRED")) {
            stateComboBox.setValue("Archivado");
        }
    }

    public void handleUpdateStudent(ActionEvent event) throws IOException {
        String selectedState = stateComboBox.getValue();
        String internalState = selectedState.equalsIgnoreCase("Activo") ? "ACTIVE" : "RETIRED";
        try {
            StudentDTO dataObjectStudent = new StudentDTO.StudentBuilder()
                    .setPaternalLastName(fieldPaternalLastName.getText())
                    .setMaternalLastName(fieldMaternalLastName.getText())
                    .setName(fieldName.getText())
                    .setID(fieldIDStudent.getText())
                    .setEmail(fieldEmail.getText())
                    .setState(internalState)
                    .setFinalGrade(previousStudent.getFinalGrade())
                    .build();

            AccountDTO existingAccount = ACCOUNT_DAO.getOne(dataObjectStudent.getEmail());
            if (existingAccount != null && !dataObjectStudent.getEmail().equals(previousStudent.getEmail())) {
                Modal.displayError("No ha sido posible actualizar al estudiante debido a que ya existe una cuenta con ese correo electrónico.");
                return;
            }

            StudentDTO existingStudent = STUDENT_DAO.getOne(dataObjectStudent.getID());
            if (existingStudent != null && !existingStudent.getID().equals(previousStudent.getID())) {
                Modal.displayError("No ha sido posible actualizar al estudiante debido a que ya existe un estudiante con la misma ID de Estudiante.");
                return;
            }

            ACCOUNT_DAO.updateOne(new AccountDTO(dataObjectStudent.getEmail(), dataObjectStudent.getID())
            );
            STUDENT_DAO.updateOne(dataObjectStudent);

            String newNRC = fieldNRC.getText().trim();
            if (!newNRC.isEmpty()) {
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                var existingEnrollment = enrollmentDAO.getOne(new FilterEnrollment(previousStudent.getID(), null));
                if (existingEnrollment != null) {
                    enrollmentDAO.deleteOne(new FilterEnrollment(previousStudent.getID(), existingEnrollment.getIDCourse()));
                }
                EnrollmentDTO newEnrollment = new EnrollmentDTO.EnrollmentBuilder()
                        .setIDCourse(newNRC)
                        .setIDStudent(dataObjectStudent.getID())
                        .build();
                enrollmentDAO.createOne(newEnrollment);
            }

            Modal.displaySuccess("Estudiante actualizado exitosamente.");
            returnToReviewStudentsPage(event);
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Modal.displayError("No ha sido posible actualizar al estudiante debido a un error de conexion con la base de datos.");
        }
    }

    @FXML
    public void returnToReviewStudentsPage(ActionEvent event) throws IOException {
        Parent newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/ReviewStudentListPage.fxml")));
        Scene newScene = new Scene(newView);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    }
}
