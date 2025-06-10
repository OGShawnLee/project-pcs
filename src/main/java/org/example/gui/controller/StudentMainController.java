package org.example.gui.controller;

import javafx.stage.Stage;
import org.example.business.dao.*;
import org.example.business.dao.filter.FilterProject;
import org.example.business.dto.*;
import org.example.gui.Modal;
import org.example.gui.Session;

import java.sql.SQLException;

public class StudentMainController extends Controller{

    public void handleUpdateStudent(){
        try {
            Object currentUser = Session.getCurrentUser();
            StudentDAO studentDAO = new StudentDAO();

            if (currentUser instanceof StudentDTO studentSession) {
                StudentDTO student = studentDAO.getOne(studentSession.getID());
                Controller.navigateToManagePage(
                        getScene(),
                        "Gestión de estudiante",
                        "UpdateStudentPage",
                        student
                );
            }

        } catch (SQLException e) {
            Modal.displayError("Error al buscar el estudiante en la base de datos.");

        }
    }

    public void handleReviewStudentProject(){
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
            if (myProject == null) {
                Modal.displayError("El proyecto asignado no pudo ser encontrado.");
                return;
            }

            ReviewStudentProjectController.navigateToUpdateStudent(getScene());

        } catch (SQLException e) {
            Modal.displayError("Ocurrion un error al intentar obtener El proyecto");
        }
        ReviewStudentProjectController.navigateToUpdateStudent(getScene());
    }

    public void navigateToRegisterSelfEvaluation() {
        try {
            Object user = Session.getCurrentUser();
            if (!(user instanceof StudentDTO currentStudent)) {
                Modal.displayError("Solo los estudiantes pueden registrar evaluaciones.");
                return;
            }

            String studentId = currentStudent.getID();

            SelfEvaluationDAO selfEvaluationDAO = new SelfEvaluationDAO();
            SelfEvaluationDTO selfEvaluationDTO = selfEvaluationDAO.getOne(studentId);

            if (selfEvaluationDTO == null) {
                RegisterSelfEvaluationController.navigateToRegisterSelfEvaluation(getScene());
            } else {
                Modal.displayError("Ya has realizado tu evaluacion");
            }

        } catch (SQLException e){
            Modal.displayError("Ocurrio un error al intentar acceder a la base de datos");
        }

    }

    public void handleRegisterProjectRequest() {
        try {
            Object user = Session.getCurrentUser();
            if (!(user instanceof StudentDTO currentStudent)) {
                Modal.displayError("Solo los estudiantes pueden registrar proyectos.");
                return;
            }

            String studentId = currentStudent.getID();

            PracticeDAO practiceDAO = new PracticeDAO();
            PracticeDTO practice = practiceDAO.getAll().stream()
                    .filter(p -> p.getIDStudent().equals(studentId))
                    .findFirst()
                    .orElse(null);

            if (practice == null) {
                RegisterProjectRequestController.navigateToRegisterProjectRequest(getScene());
                return;
            }

            ProjectDAO projectDAO = new ProjectDAO();
            ProjectDTO projectDTO = projectDAO.getOne(practice.getIDProject());

            if (projectDTO != null) {
                Modal.displayError("Ya tienes un proyecto asignado");
                return;
            }

            FilterProject filterProject = new FilterProject(projectDTO.getID(), studentId);

            ProjectRequestDAO requestDAO = new ProjectRequestDAO();
            ProjectRequestDTO existingRequest = requestDAO.getOne(filterProject);

            if (existingRequest != null) {
                Modal.displayError("Ya realizaste una solicitud de projecto de proyecto.");
            } else {
                RegisterProjectRequestController.navigateToRegisterProjectRequest(getScene());
            }

        } catch (SQLException e) {
            Modal.displayError("Ha ocurrido un error con la base de datos");
        }
    }



    public void handleRegisterMonthlyReportAction(){

    }

    public static void navigateToStudentMain(Stage currentStage) {
        navigateTo(currentStage, "Página de Inicio Estudiante", "StudentMainPage");
    }
}