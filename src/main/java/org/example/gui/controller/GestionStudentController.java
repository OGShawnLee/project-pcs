package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class GestionStudentController {

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

    private StudentDTO currentStudent;

    public void setStudent(StudentDTO student) {
        this.currentStudent = student;

        idTextField.setText(student.getID());
        nameTextField.setText(student.getName());
        paternalLastNameTextField.setText(student.getPaternalLastName());
        maternalLastNameTextField.setText(student.getMaternalLastName());
        emailTextField.setText(student.getEmail());
    }

    @FXML
    public void updateStudentData(ActionEvent event) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
                    .setID(currentStudent.getID())
                    .setName(nameTextField.getText())
                    .setPaternalLastName(paternalLastNameTextField.getText())
                    .setMaternalLastName(maternalLastNameTextField.getText())
                    .setEmail(emailTextField.getText())
                    .setState(currentStudent.getState())
                    .build();

            studentDAO.updateOne(updatedStudent);

            AlertDialog.showSuccess("Datos actualizados correctamente.");

        } catch (SQLException e) {
            AlertDialog.showError("No se pudieron actualizar los datos.");
        }
    }
}