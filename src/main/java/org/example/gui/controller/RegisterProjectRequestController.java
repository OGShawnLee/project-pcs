package org.example.gui.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.ProjectRequestDTO;
import org.example.business.dao.ProjectRequestDAO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;
import org.example.gui.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterProjectRequestController extends Controller{

    @FXML
    private TableView<ProjectDTO> projectRequestTable;
    @FXML
    private TableColumn<ProjectDTO, String> nameColumn;
    @FXML
    private TableColumn<ProjectDTO, String> emailColumn;
    @FXML
    private TableColumn<ProjectDTO, String> methodologyColumn;
    @FXML
    private TableColumn<ProjectDTO, String> sectorColumn;
    @FXML
    private TableColumn<ProjectDTO, Boolean> checkBoxColumn;

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final List<ProjectDTO> selectedProjects = new ArrayList<>();
    private final HashMap<ProjectDTO, BooleanProperty> selectionMap = new HashMap<>();
    private final ProjectRequestDAO PROJECT_REQUEST_DAO = new ProjectRequestDAO();

    @FXML
    public void initialize() {
        projectRequestTable.setEditable(true);
        checkBoxColumn.setEditable(true);

        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        emailColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getIDOrganization()));
        methodologyColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMethodology()));
        sectorColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSector()));

        checkBoxColumn.setCellValueFactory(cellData -> {
            ProjectDTO projectDTO = cellData.getValue();
            BooleanProperty property;

            property = selectionMap.computeIfAbsent(projectDTO, project -> {
                BooleanProperty newProperty = new SimpleBooleanProperty(false);

                newProperty.addListener((observer, oldValue, newValue) -> {
                    if (newValue) {
                        if (selectedProjects.size() >= 3) {
                            Platform.runLater(() -> newProperty.set(false));
                        } else {
                            selectedProjects.add(projectDTO);
                        }
                    } else {
                        selectedProjects.remove(projectDTO);
                    }
                });
                return newProperty;
            });
            return property;
        });

        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

        updateTable();
    }

    @FXML
    public void handleRegisterProjectRequest () {
        Object user = Session.getCurrentUser();
        if (!(user instanceof StudentDTO student)) {
            Modal.displayError("Solo es una solicitud de proyecto por estudiante.");
            return;
        }

        if (selectedProjects.size() != 3) {
            Modal.displayError("Debes seleccionar exactamente 3 proyectos.");
            return;
        }

        try {
            for (ProjectDTO project : selectedProjects) {
                ProjectRequestDTO newRequest = new ProjectRequestDTO.ProjectRequestBuilder()
                        .setIDStudent(student.getID())
                        .setIDProject(project.getID())
                        .setState("PENDING")
                        .setReasonOfState("Solicitud inicial del estudiante")
                        .setCreatedAt(java.time.LocalDateTime.now())
                        .build();

                PROJECT_REQUEST_DAO.createOne(newRequest);
            }
            Modal.displaySuccess("La autoevaluacion ha sido registrada exitosamente.");
            StudentMainController.navigateToStudentMain(getScene());
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible registrar la organización debido a un error de sistema.");
        }
    }

    public void updateTable() {
        try {
            List<ProjectDTO> projectList = projectDAO.getAll();
            ObservableList<ProjectDTO> observableList = FXCollections.observableArrayList(projectList);
            projectRequestTable.setItems(observableList);
        } catch (SQLException e) {
            Modal.displayError("No se pudo actualizar la tabla debido a un error en el sistema.");
        }
    }

    public void goBack() {
        StudentMainController.navigateToStudentMain(getScene());
    }

    public static void navigateToRegisterProjectRequest(Stage currentStage) {
        navigateTo(currentStage, "Registrar Solicitud de Proyecto", "RegisterProjectRequestPage");
    }
}
