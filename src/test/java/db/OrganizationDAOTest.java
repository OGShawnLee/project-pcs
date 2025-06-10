package db;

import org.example.business.dto.OrganizationDTO;
import org.example.business.dao.OrganizationDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OrganizationDAOTest {
  public static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  public static final OrganizationDTO ORGANIZATION_DTO = new OrganizationDTO.OrganizationBuilder()
    .setEmail("microsoftest@outlook.com")
    .setName("Microsoft")
    .setRepresentativeFullName("Bill Gates")
    .setColony("Redmond")
    .setStreet("Microsoft Way")
    .setState("ACTIVE")
    .build();

  public static void createOneTestOrganization() throws SQLException {
    ORGANIZATION_DAO.createOne(ORGANIZATION_DTO);
  }

  public static void deleteOneTestOrganization() throws SQLException {
    ORGANIZATION_DAO.deleteOne(ORGANIZATION_DTO.getEmail());
  }

  @AfterEach
  public void tearDown() throws SQLException {
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
        .setRepresentativeFullName("Steve Jobs")
        .setColony("Cupertino")
        .setStreet("Infinite Loop")
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
