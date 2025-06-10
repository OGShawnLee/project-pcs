package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.business.dao.MonthlyReportDAO;
import org.example.business.dao.PracticeDAO;
import org.example.business.dto.MonthlyReportDTO;
import org.example.business.dto.PracticeDTO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;
import org.example.gui.Session;


import java.io.File;
import java.sql.SQLException;

public class RegisterMonthlyReportController extends Controller {

    @FXML private TextField fieldReportNumber;
    @FXML private ComboBox<String> comboMonth;
    @FXML private Button btnSelectFile;
    @FXML private Label lblFileName;

    private File selectedFile;
    private final MonthlyReportDAO monthlyReportDAO = new MonthlyReportDAO();

    @FXML
    public void initialize() {
        comboMonth.getItems().addAll(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        );

        btnSelectFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar archivo de reporte");
            selectedFile = fileChooser.showOpenDialog(getStage());

            if (selectedFile != null) {
                lblFileName.setText("Nombre del archivo: " + selectedFile.getName());
            } else {
                lblFileName.setText("Nombre del archivo:");
            }
        });
    }

    public void handleRegisterReport() {
        try {
            int numberText = Integer.parseInt(fieldReportNumber.getText().trim());
            String month = comboMonth.getValue();



            StudentDTO student = (StudentDTO) Session.getCurrentUser();
            if (student == null) {
                Modal.displayError("No se pudo obtener el usuario actual.");
                return;
            }
            PracticeDAO practiceDAO = new PracticeDAO();
            PracticeDTO practice = practiceDAO.getAll().stream()
                    .filter(practiceDTO -> practiceDTO.getIDStudent().equals(student.getID()))
                    .findFirst()
                    .orElse(null);
            if (practice == null) {
                Modal.displayError("No tienes un proyecto asignado actualmente.");
                return;
            }

            MonthlyReportDTO report = new MonthlyReportDTO.MonthlyReportBuilder()
                    .setIDStudent(student.getID())
                    .setIDProject(practice.getIDProject())
                    .setWorkedHours(numberText)
                    .setIDStudent(student.getID())
                    .setMonth(Integer.parseInt(month))
                    .setReport(String.valueOf(selectedFile))
                    .build();

            monthlyReportDAO.createOne(report);
            Modal.displaySuccess("Reporte mensual registrado exitosamente.");

        } catch (SQLException e) {
            Modal.displayError("No fue posible registrar el reporte debido a un error de sistema.");
            e.printStackTrace();
        }
    }

    public void goBack() {
        StudentMainController.navigateToStudentMain(getScene());
    }

    public static void navigateToRegisterMonthlyReportPage(Stage currentStage) {
        navigateTo(currentStage, "Registrar Reporte Mensual", "RegisterMonthlyReportPage");
    }

    private Stage getStage() {
        return (Stage) btnSelectFile.getScene().getWindow();
    }
}
