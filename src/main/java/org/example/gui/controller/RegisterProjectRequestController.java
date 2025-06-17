package org.example.gui.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.business.dao.ProjectDAO;
import org.example.business.dao.ProjectSector;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.ProjectRequestDTO;
import org.example.business.dao.ProjectRequestDAO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterProjectRequestController {

    @FXML
    private TableView<ProjectDTO> projectRequestTable;
    @FXML
    private TableColumn<ProjectDTO, String> nameColumn;
    @FXML
    private TableColumn<ProjectDTO, String> emailColumn;
    @FXML
    private TableColumn<ProjectDTO, String> metodologyColumn;
    @FXML
    private TableColumn<ProjectDTO, ProjectSector> sectorColumn;
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
        metodologyColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMethodology()));
        sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sector"));

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
    public void handleRegisterProjectRequest (ActionEvent event) {
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
            returnToMainPage(event);
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible registrar la organización debido a un error de sistema.");
        } catch (IOException e) {
            Modal.displayError("Ha ocurrido un error al cargar la pagina");
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

    @FXML
    public void returnToMainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/LandingStudentPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Menu Principal");
        stage.show();
    }
}
