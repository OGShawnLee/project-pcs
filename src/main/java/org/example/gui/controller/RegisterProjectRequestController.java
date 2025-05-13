package org.example.gui.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.example.business.dao.ProjectDAO;
import org.example.business.dto.ProjectDTO;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class RegisterProjectRequestController {

    @FXML
    private TableView<ProjectDTO> projectRequestTable;
    @FXML private TableColumn<ProjectDTO, String> nameColumn;
    @FXML private TableColumn<ProjectDTO, String> emailColumn;
    @FXML private TableColumn<ProjectDTO, String> metodologyColumn;
    @FXML private TableColumn<ProjectDTO, String> sectorColumn;
    @FXML private TableColumn<ProjectDTO, Boolean> checkBoxColumn;

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final List<ProjectDTO> selectedProjects = new ArrayList<>();
    private final HashMap<ProjectDTO, BooleanProperty> selectionMap = new HashMap<>();

    @FXML
    public void initialize() {
        projectRequestTable.setEditable(true);
        checkBoxColumn.setEditable(true);

        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        emailColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getIDOrganization()));
        metodologyColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getMethodology()));
        sectorColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSector()));

        checkBoxColumn.setCellValueFactory(cellData -> {
            ProjectDTO project = cellData.getValue();
            BooleanProperty property = selectionMap.computeIfAbsent(project, p -> new SimpleBooleanProperty(false));

            property.addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    if (selectedProjects.size() >= 3) {
                        property.set(false);
                        return;
                    }

                    selectedProjects.add(project);
                } else {
                    selectedProjects.remove(project);
                }
            });

            return property;
        });

        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

        updateTable();
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

    public List<ProjectDTO> getSelectedProjects() {
        return new ArrayList<>(selectedProjects);
    }

    @FXML
    public void returnToMainPage(ActionEvent event) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/example/StudentMainPage.fxml"));
        javafx.scene.Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root));
        stage.setTitle("Menu Principal");
        stage.show();
    }
}