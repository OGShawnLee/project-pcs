package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.auth.AuthClient;
import org.example.business.dao.*;
import org.example.business.dto.*;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ReviewEvaluationListController extends ReviewListController {
    private static final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();
    private static final StudentDAO STUDENT_DAO = new StudentDAO();
    private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();

    @FXML
    private TableView<EvaluationDTO> tableEvaluation;
    @FXML
    private TableColumn<EvaluationDTO, String> columnIDStudent;
    @FXML
    private TableColumn<EvaluationDTO, String> columnIDAcademic;
    @FXML
    private TableColumn<EvaluationDTO, String> columnIDProject;
    @FXML
    private TableColumn<EvaluationDTO, String> columnCreatedAt;

    public void initialize() {
        loadDataList();
    }

    @Override
    public void loadDataList() {
        AccountDTO.Role currentUserType = AuthClient.getInstance().getCurrentUser().role();
        switch (currentUserType) {
            case STUDENT:
                loadStudentView();
            case ACADEMIC:
                loadAcademicView();
                break;
            case EVALUATOR:
                loadEvaluatorView();
                break;
            case ACADEMIC_EVALUATOR:
                loadAcademicEvaluatorView();
                break;
        }
    }

    public void loadStudentView() {
        try {
            StudentDTO student = STUDENT_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());
            tableEvaluation.setItems(FXCollections.observableList(
                    EVALUATION_DAO.getAllByStudent(student.getID())
            ));
        } catch (SQLException e){
            Modal.displayError("No fue posible obtener las evaluaciones del usuario estudiante");
        }
    }

    public void loadAcademicView() {
        try {
            AcademicDTO academic = ACADEMIC_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());
            tableEvaluation.setItems(FXCollections.observableList(EVALUATION_DAO.getAllByAcademic(academic.getID())));
        } catch (SQLException e) {
            Modal.displayError("No fue posible obtener las evaluaciones del usuario academico");
        }
    }

    public void loadEvaluatorView() {
        try {
            AcademicDTO evaluator = ACADEMIC_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());
            tableEvaluation.setItems(FXCollections.observableList(
                    EVALUATION_DAO.getAllByEvaluators(evaluator.getID())
            ));
        } catch (SQLException e) {
            Modal.displayError("No fue posible obtener las evaluaciones del usuario evaluador");
        }
    }

    public void loadAcademicEvaluatorView() {
        try {
            AcademicDTO academicAndEvaluator = ACADEMIC_DAO.getOneByEmail(AuthClient.getInstance().getCurrentUser().email());
            Set<EvaluationDTO> combined = new LinkedHashSet<>();
            combined.addAll( EVALUATION_DAO.getAllByAcademic(academicAndEvaluator.getID()));
            combined.addAll(EVALUATION_DAO.getAllByEvaluators(academicAndEvaluator.getID()));

            tableEvaluation.setItems(FXCollections.observableList(new ArrayList<>(combined)));
        } catch (SQLException e) {
            Modal.displayError("No fue posible obtener las evalujaciones del usuario academico-evaluador");
        }
    }

    @Override
    public void loadTableColumns() {
        columnIDAcademic.setCellValueFactory(new PropertyValueFactory<>("id_academic"));
        columnIDProject.setCellValueFactory(new PropertyValueFactory<>("id_project"));
        columnIDStudent.setCellValueFactory(new PropertyValueFactory<>("id_student"));
        columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    public void handleOpenRegisterEvaluationModal() {
        Modal.display(
                "Registrar Evaluacion",
                "RegisterEvaluationModal",
                this::loadDataList
        );
    }

    public void handleVisualizeEvaluationModal() {
        EvaluationDTO selectedEvaluation = tableEvaluation.getSelectionModel().getSelectedItem();

        if (selectedEvaluation == null) return;

        Modal.displayManageModal(
                "Visualizar Evaluacion",
                "VisualizeEvaluationModal",
                this::loadDataList,
                selectedEvaluation
        );
    }

    public static void navigateToEvaluationListPage(Stage currentStage) {
        navigateTo(currentStage, "Lista de Evaluaciones", "ReviewEvaluationListPage");
    }
}
