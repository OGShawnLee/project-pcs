package org.example.gui.controller;

import javafx.scene.control.ComboBox;

import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.RepresentativeDTO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.gui.Modal;

import java.sql.SQLException;
import java.util.List;

public class ComboBoxLoader {
  public static void loadComboBoxOrganization(
    ComboBox<OrganizationDTO> comboBoxOrganization,
    boolean isWithRepresentatives
  ) {
    try {
      OrganizationDAO organizationDAO = new OrganizationDAO();
      List<OrganizationDTO> organizationList = isWithRepresentatives
        ? organizationDAO.getAllWithRepresentatives()
        : organizationDAO.getAllByState("ACTIVE");

      if (isWithRepresentatives) {
        if (organizationList.isEmpty()) {
          Modal.displayError("No existe una organización con representantes. Por favor, registre un representante antes de continuar.");
          return;
        }
      } else if (organizationList.isEmpty()) {
        Modal.displayError("No existe una organización. Por favor, registre una organización antes de continuar.");
        return;
      }

      comboBoxOrganization.getItems().addAll(organizationList);
      comboBoxOrganization.setValue(organizationList.getFirst());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
    }
  }

  public static void loadRecordState(ComboBox<String> fieldState) {
    fieldState.getItems().setAll("Activo", "Inactivo");
    fieldState.setValue("Activo");
  }

  public static void loadComboBoxRepresentativeByOrganization(
    ComboBox<RepresentativeDTO> comboBoxRepresentative,
    OrganizationDTO organization
  ) {
    try {
      List<RepresentativeDTO> representativeList = new RepresentativeDAO().getAllByOrganization(organization, "ACTIVE");

      if (representativeList.isEmpty()) {
        Modal.displayError("No existe un representante. Por favor, registre un representante antes de continuar.");
        Modal.display(
          "Registrar Representante",
          "RegisterRepresentativeModal",
          () -> loadComboBoxRepresentativeByOrganization(comboBoxRepresentative, organization)
        );
        return;
      }

      comboBoxRepresentative.getItems().addAll(representativeList);
      comboBoxRepresentative.setValue(representativeList.getFirst());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar los representantes debido a un error en el sistema.");
    }
  }

  public static void loadRepresentativeComboBoxFromOrganizationComboBoxSelection(
    ComboBox<OrganizationDTO> comboBoxOrganization,
    ComboBox<RepresentativeDTO> comboBoxRepresentative
  ) {
    loadComboBoxRepresentativeByOrganization(comboBoxRepresentative, comboBoxOrganization.getValue());
    comboBoxOrganization.setOnAction(event -> {
      OrganizationDTO selectedOrganization = comboBoxOrganization.getValue();
      if (selectedOrganization != null) {
        try {
          List<RepresentativeDTO> representatives = new RepresentativeDAO().getAllByOrganization(selectedOrganization, "ACTIVE");
          comboBoxRepresentative.getItems().setAll(representatives);

          if (!representatives.isEmpty()) {
            comboBoxRepresentative.setValue(representatives.get(0));
          }
        } catch (SQLException e) {
          Modal.displayError("No ha sido posible cargar los representantes debido a un error en el sistema.");
        }
      } else {
        comboBoxRepresentative.getItems().clear();
      }
    });
  }

  public static void loadComboBoxSector(ComboBox<ProjectSector> comboBoxSector) {
    comboBoxSector.getItems().addAll(ProjectSector.values());
    comboBoxSector.setValue(ProjectSector.PUBLIC);
  }
}
