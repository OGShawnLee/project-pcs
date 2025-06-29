package org.example.gui.controller;

import javafx.scene.control.ComboBox;

import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.RepresentativeDTO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

import java.util.List;

public class ComboBoxLoader {
  private static <T> void loadComboBoxAndSetDefault(ComboBox<T> comboBox, List<T> list) {
    comboBox.getItems().addAll(list);
    comboBox.setValue(list.getFirst());
  }

  public static void loadComboBoxOrganizationWithRepresentatives(
    ComboBox<OrganizationDTO> comboBoxOrganization
  ) {
    try {
      OrganizationDAO organizationDAO = new OrganizationDAO();
      List<OrganizationDTO> organizationDTOList = organizationDAO.getAllWithRepresentatives();

      if (organizationDTOList.isEmpty()) {
       Modal.displayError("No existen organizaciones con representantes. Por favor, registre un representante antes de continuar.");
       return;
      }

      loadComboBoxAndSetDefault(comboBoxOrganization, organizationDTOList);
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible cargar las organizaciones que tienen representantes debido a un error en el sistema.");
    }
  }

  public static void loadComboBoxOrganization(ComboBox<OrganizationDTO> comboBoxOrganization) {
    try {
      OrganizationDAO organizationDAO = new OrganizationDAO();
      List<OrganizationDTO> organizationDTOList = organizationDAO.getAllByState("ACTIVE");

      if (organizationDTOList.isEmpty()) {
        Modal.displayError("No existe una organización. Por favor, registre una organización antes de continuar.");
        return;
      }

      loadComboBoxAndSetDefault(comboBoxOrganization, organizationDTOList);
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible cargar las organizaciones debido a un error en el sistema.");
    }
  }

  public static void loadComboBoxState(ComboBox<String> comboBoxState) {
    comboBoxState.getItems().setAll("Activo", "Inactivo");
    comboBoxState.setValue("Activo");
  }

  public static void loadComboBoxRepresentativeByOrganization(ComboBox<RepresentativeDTO> comboBoxRepresentative, String email) {
    try {
      List<RepresentativeDTO> representativeDTOList = new RepresentativeDAO().getAllByOrganization(email, "ACTIVE");

      if (representativeDTOList.isEmpty()) {
        Modal.displayError("No existe un representante. Por favor, registre un representante antes de continuar.");
        Modal.display(
          "Registrar Representante",
          "RegisterRepresentativeModal",
          () -> loadComboBoxRepresentativeByOrganization(comboBoxRepresentative, email)
        );
        return;
      }

      loadComboBoxAndSetDefault(comboBoxRepresentative, representativeDTOList);
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible cargar los representantes debido a un error en el sistema.");
    }
  }

  public static void loadSynchronizationOfRepresentativeComboBoxFromOrganizationComboBoxSelection(
    ComboBox<OrganizationDTO> comboBoxOrganization,
    ComboBox<RepresentativeDTO> comboBoxRepresentative
  ) {
    comboBoxOrganization.setOnAction(event -> {
      OrganizationDTO selectedOrganizationDTO = comboBoxOrganization.getValue();

      if (selectedOrganizationDTO == null) {
        comboBoxRepresentative.getItems().clear();
        return;
      }

      try {
        List<RepresentativeDTO> representatives = new RepresentativeDAO().getAllByOrganization(selectedOrganizationDTO.getEmail(), "ACTIVE");
        comboBoxRepresentative.getItems().setAll(representatives);

        if (representatives.isEmpty()) {
          return;
        }

        comboBoxRepresentative.setValue(representatives.get(0));
      } catch (UserDisplayableException e) {
        Modal.displayError("No ha sido posible cargar los representantes debido a un error en el sistema.");
      }
    });
  }

  public static void loadComboBoxSector(ComboBox<ProjectSector> comboBoxSector) {
    loadComboBoxAndSetDefault(comboBoxSector, List.of(ProjectSector.values()));
  }
}
