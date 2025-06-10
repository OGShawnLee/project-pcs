package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class RegisterFinalGradeController extends ManageController<StudentDTO> {
    private final StudentDAO STUDENT_DAO = new StudentDAO();

    @FXML
    private TextField finalGradeField;

    @Override
    protected void initialize(StudentDTO student) {
        super.initialize(student);
        if (student != null) {
            finalGradeField.setText(String.valueOf(student.getFinalGrade()));
        } else {
            Modal.displayError("El estudiante no está disponible.");
        }
    }

    @FXML
    @Override
    protected void handleUpdateCurrentDataObject() {
        try {
            StudentDTO previousStudent = getCurrentDataObject();
            int newFinalGrade = Integer.parseInt(finalGradeField.getText());

            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(previousStudent.getID())
                    .setName(previousStudent.getName())
                    .setPaternalLastName(previousStudent.getPaternalLastName())
                    .setMaternalLastName(previousStudent.getMaternalLastName())
                    .setEmail(previousStudent.getEmail())
                    .setState(previousStudent.getState())
                    .setFinalGrade(newFinalGrade)
                    .build();
            STUDENT_DAO.updateOne(updatedStudent);

            Modal.displaySuccess("La calificación fue asignada correctamente.");
            navigateToStudentList();
        } catch (NumberFormatException e) {
            Modal.displayError("La calificación ingresada no es válida.");
        } catch (SQLException e){
            Modal.displayError("No se pudieron actualizar los datos.");
        }
    }

    public void navigateToStudentList() {
        ReviewStudentListController.navigateToStudentListPage(getScene());
    }

    public static void navigateToRegisterFinalGradePage(Stage currentStage) {
        navigateTo(currentStage, "Registro de Calificación Final", "RegisterFinalGradePage");
    }
}
