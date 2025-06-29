package org.example.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.example.business.dto.EvaluationDTO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.EvaluationDAO;
import org.example.business.dao.StudentDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import java.io.IOException;
import java.util.List;

// TODO: UPDATE USE CASE
public class ReviewEvaluationListController {
  @FXML
  private TableView<EvaluationDTO> evaluationTable;
  @FXML
  private TableColumn<EvaluationDTO, String> idColumn;
  @FXML
  private TableColumn<EvaluationDTO, Button> visualizerColumn;
  @FXML
  private TableColumn<EvaluationDTO, String> nameColumn;
  @FXML
  private TableColumn<EvaluationDTO, String> nrcColumn;

  private final EvaluationDAO evaluationDAO = new EvaluationDAO();

  @FXML
  public void showTableEvaluations() {
    idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIDStudent()));
    nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getFullName(cellData.getValue().getIDStudent())));
    nrcColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getNrc(cellData.getValue().getIDStudent())));

    visualizerColumn.setCellFactory(col -> new TableCell<>() {
      private final Button button = new Button("Visualizar");

      @Override
      protected void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || getTableRow() == null || getTableRow().getItem() == null ? null : createButtonForEvaluation(getTableRow().getItem()));
      }

      private Button createButtonForEvaluation(Object item) {
        EvaluationDTO evaluation = (EvaluationDTO) item;
        if (evaluation.getIDStudent() != null && !evaluation.getIDStudent().isBlank()) {
          button.setOnAction(event -> openEvaluationDetailsPage(evaluation));
          return button;
        }
        return null;
      }
    });

    loadEvaluations();
  }

  private String getFullName(String idStudent) {
    String fullName = "Desconocido";
    try {
      StudentDAO studentDAO = new StudentDAO();
      var student = studentDAO.getOne(idStudent);
      if (student != null) {
        fullName = student.getName() + " " + student.getPaternalLastName() + " " + student.getMaternalLastName();
      }
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible conectar con los datos del estudiante, debido a un error de conexion con la base de datos");
    }
    return fullName;
  }

  private String getNrc(String idStudent) {
    String nrc = "No inscrito";
    try {
      EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
      var enrollmentList = enrollmentDAO.getAll();
      for (var enrollment : enrollmentList) {
        if (enrollment.getIDStudent().equals(idStudent)) {
          nrc = enrollment.getIDCourse();
          break;
        }
      }
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible conectar con la materia asignada, debido a un error de conexion con la base de datos");
    }
    return nrc;
  }

  private void openEvaluationDetailsPage(EvaluationDTO evaluation) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/VisualizeEvaluationPage.fxml"));
      Parent root = loader.load();

      VisualizeEvaluationController controller = loader.getController();
      controller.setEvaluation(evaluation);

      Stage stage = new Stage();
      stage.setTitle("Detalles de la Evaluación");
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      AlertFacade.showErrorAndWait("No se pudo cargar la vista de detalles.");
    }
  }

  private void loadEvaluations() {
    try {
      List<EvaluationDTO> evaluationList = evaluationDAO.getAll();
      ObservableList<EvaluationDTO> observableList = FXCollections.observableArrayList(evaluationList);
      evaluationTable.setItems(observableList);
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }


  public void updateTable() {
    try {
      List<EvaluationDTO> evaluationList = evaluationDAO.getAll();
      ObservableList<EvaluationDTO> observableList = FXCollections.observableArrayList(evaluationList);
      evaluationTable.setItems(observableList);
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  @FXML
  public void initialize() {
    showTableEvaluations();
  }
}
