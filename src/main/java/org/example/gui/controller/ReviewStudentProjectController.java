package org.example.gui.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.business.dao.PracticeDAO;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.PracticeDTO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dao.OrganizationDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.gui.Modal;
import org.example.gui.Session;


public class ReviewStudentProjectController extends Controller{

    @FXML
    private Label labelProjectName;
    @FXML
    private Label labelOrganization;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelMethodology;
    @FXML
    private Label labelSector;

    public void initialize() {
        try {
            Object user = Session.getCurrentUser();
            if (!(user instanceof StudentDTO studentDTO)) {
                Modal.displayError("El usuario actual no es un estudiante.");
                return;
            }

            PracticeDAO practiceDAO = new PracticeDAO();
            PracticeDTO practice = practiceDAO.getAll().stream()
                    .filter(practiceDTO -> practiceDTO.getIDStudent().equals(studentDTO.getID()))
                    .findFirst()
                    .orElse(null);

            if (practice == null) {
                Modal.displayError("No tienes un proyecto asignado actualmente.");
                return;
            }

            ProjectDTO myProject = new ProjectDAO().getOne(practice.getIDProject());
            OrganizationDTO organization = new OrganizationDAO().getOne(myProject.getIDOrganization());

            labelProjectName.setText(myProject.getName());
            labelOrganization.setText(organization.getName());
            labelMethodology.setText(myProject.getMethodology());
            labelSector.setText(myProject.getSector());
            labelEmail.setText(myProject.getIDOrganization());

        } catch (Exception e) {
            Modal.displayError("Error al cargar el proyecto asignado.");
        }
    }

    public void navigateToStudentMain() {
        StudentMainController.navigateToStudentMain(getScene());
    }

    public static void navigateToUpdateStudent(Stage currentStage) {
        navigateTo(currentStage, "Proyecto de Estudiante", "ReviewStudentProjectPage");
    }

}