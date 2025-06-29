package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.business.dto.OrganizationDTO;
import org.example.business.dao.OrganizationDAO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class ReviewOrganizationListController extends ReviewListController implements FilterableByStateController {
  private static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TableView<OrganizationDTO> tableOrganization;
  @FXML
  private TableColumn<OrganizationDTO, String> columnEmail;
  @FXML
  private TableColumn<OrganizationDTO, String> columnName;
  @FXML
  private TableColumn<OrganizationDTO, String> columnAddress;
  @FXML
  private TableColumn<OrganizationDTO, String> columnPhoneNumber;
  @FXML
  private TableColumn<OrganizationDTO, String> columnState;
  @FXML
  private TableColumn<OrganizationDTO, String> columnCreatedAt;

  @Override
  public void loadTableColumns() {
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
  }

  @Override
  public void loadDataList() {
    try {
      tableOrganization.setItems(
        FXCollections.observableList(
          ORGANIZATION_DAO.getAll()
        )
      );
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible cargar información de organizaciones debido a un error en el sistema."
      );
    }
  }

  @Override
  public void loadDataListByActiveState() {
    try {
      tableOrganization.setItems(
        FXCollections.observableList(
          ORGANIZATION_DAO.getAllByState("ACTIVE")
        )
      );
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible cargar información de organizaciones activas debido a un error en el sistema."
      );
    }
  }

  @Override
  public void loadDataListByInactiveState() {
    try {
      tableOrganization.setItems(
        FXCollections.observableList(
          ORGANIZATION_DAO.getAllByState("INACTIVE")
        )
      );
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible cargar información de organizaciones inactivas debido a un error en el sistema."
      );
    }
  }

  public void handleOpenRegisterOrganizationModal() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration(
        "Registrar Organización",
        "RegisterOrganizationModal",
        this::loadDataList
      )
    );
  }

  public void handleManageOrganization() {
    OrganizationDTO selectedOrganization = tableOrganization.getSelectionModel().getSelectedItem();

    if (selectedOrganization == null) return;

    ModalFacade.createAndDisplayContextModal(
      new ModalFacadeConfiguration(
        "Gestionar Organización",
        "ManageOrganizationModal",
        this::loadDataList
      ),
      selectedOrganization
    );
  }

  public static void navigateToOrganizationListPage(Stage currentStage) {
    navigateTo(currentStage, "Listado de Acádemicos", "ReviewOrganizationListPage");
  }
}
