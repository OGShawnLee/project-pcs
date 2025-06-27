package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.business.dto.RepresentativeDTO;
import org.example.business.dao.RepresentativeDAO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ReviewRepresentativeListController extends ReviewListController implements FilterableByStateController {
  private static final RepresentativeDAO REPRESENTATIVE_DAO = new RepresentativeDAO();
  @FXML
  private TableView<RepresentativeDTO> tableRepresentative;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnEmail;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnOrganizationEmail;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnName;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnPaternalLastName;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnMaternalLastName;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnPhoneNumber;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnState;
  @FXML
  private TableColumn<RepresentativeDTO, String> columnCreatedAt;

  @Override
  public void loadTableColumns() {
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnOrganizationEmail.setCellValueFactory(new PropertyValueFactory<>("organizationID"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnPaternalLastName.setCellValueFactory(new PropertyValueFactory<>("paternalLastName"));
    columnMaternalLastName.setCellValueFactory(new PropertyValueFactory<>("maternalLastName"));
    columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
  }

  @Override
  public void loadDataList() {
    try {
      tableRepresentative.setItems(
        FXCollections.observableList(
          REPRESENTATIVE_DAO.getAll()
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de representantes debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByActiveState() {
    try {
      tableRepresentative.setItems(
        FXCollections.observableList(
          REPRESENTATIVE_DAO.getAllByState("ACTIVE")
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de representantes activos debido a un error de sistema."
      );
    }
  }

  @Override
  public void loadDataListByInactiveState() {
    try {
      tableRepresentative.setItems(
        FXCollections.observableList(
          REPRESENTATIVE_DAO.getAllByState("RETIRED")
        )
      );
    } catch (SQLException e) {
      Modal.displayError(
        "No ha sido posible cargar información de representantes inactivos debido a un error de sistema."
      );
    }
  }

  public void handleOpenRegisterRepresentativeModal() {
    Modal.display(
      "Registrar Representante",
      "RegisterRepresentativeModal",
      this::loadDataList
    );
  }

  public void handleManageRepresentative() {
    RepresentativeDTO selectedRepresentative = tableRepresentative.getSelectionModel().getSelectedItem();

    if (selectedRepresentative == null) return;

    Modal.displayContextModal(
      "Gestionar Representante",
      "ManageRepresentativeModal",
      this::loadDataList,
      selectedRepresentative
    );
  }

  public static void navigateToRepresentativeListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Representantes", "ReviewRepresentativeListPage");
  }
}
