package db;

import org.example.business.dto.OrganizationDTO;
import org.example.business.dao.OrganizationDAO;
import org.example.common.UserDisplayableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OrganizationDAOTest {
  public static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  public static final OrganizationDTO ORGANIZATION_DTO = new OrganizationDTO.OrganizationBuilder()
    .setEmail("microsoftest@outlook.com")
    .setName("Microsoft")
    .setAddress("Redmond, Washington")
    .setPhoneNumber("+1 425-882-8080")
    .setState("ACTIVE")
    .build();

  public static void createOneTestOrganization() throws UserDisplayableException {
    ORGANIZATION_DAO.createOne(ORGANIZATION_DTO);
  }

  public static void deleteOneTestOrganization() throws UserDisplayableException {
    ORGANIZATION_DAO.deleteOne(ORGANIZATION_DTO.getEmail());
  }

  @AfterEach
  public void tearDown() throws UserDisplayableException {
    deleteOneTestOrganization();
  }

  @Test
  public void testCreateOneOrganization() {
    assertDoesNotThrow(() -> {
      createOneTestOrganization();
      Assertions.assertEquals(ORGANIZATION_DTO, ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail()));
    });
  }

  @Test
  public void testGetAllOrganizations() {
    assertDoesNotThrow(() -> {
      createOneTestOrganization();

      List<OrganizationDTO> organizationList = ORGANIZATION_DAO.getAll();

      Assertions.assertNotNull(organizationList);
      Assertions.assertFalse(organizationList.isEmpty());
    });
  }

  @Test
  public void testGetOneOrganization() {
    assertDoesNotThrow(() -> {
      createOneTestOrganization();
      Assertions.assertEquals(ORGANIZATION_DTO, ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail()));
    });
  }

  @Test
  public void testUpdateOneOrganization() {
    assertDoesNotThrow(() -> {
      createOneTestOrganization();

      OrganizationDTO updatedOrganization = new OrganizationDTO.OrganizationBuilder()
        .setEmail(ORGANIZATION_DTO.getEmail())
        .setName("Apple Inc.")
        .setAddress("Cupertino, California")
        .setPhoneNumber("+1 408-996-1010")
        .setState("RETIRED")
        .build();

      ORGANIZATION_DAO.updateOne(updatedOrganization);
      Assertions.assertEquals(updatedOrganization, ORGANIZATION_DAO.getOne(updatedOrganization.getEmail()));
    });
  }

  @Test
  public void testDeleteOneOrganization() {
    assertDoesNotThrow(() -> {
      createOneTestOrganization();
      OrganizationDTO createdOrganization = ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail());
      Assertions.assertNotNull(createdOrganization);

      deleteOneTestOrganization();
      OrganizationDTO deletedOrganization = ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail());
      Assertions.assertNull(deletedOrganization);
    });
  }
}
