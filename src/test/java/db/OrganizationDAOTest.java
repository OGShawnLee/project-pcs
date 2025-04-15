package db;

import org.example.business.OrganizationDTO;
import org.example.db.OrganizationDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OrganizationDAOTest {
  public static final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  public static final OrganizationDTO ORGANIZATION_DTO = new OrganizationDTO.OrganizationBuilder()
    .setEmail("microsoft@outlook.com")
    .setName("Microsoft")
    .setRepresentativeFullName("Bill Gates")
    .setColony("Redmond")
    .setStreet("Microsoft Way")
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

      OrganizationDTO createdOrganization = ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail());

      Assertions.assertNotNull(createdOrganization);
      Assertions.assertEquals(ORGANIZATION_DTO.getEmail(), createdOrganization.getEmail());
      Assertions.assertEquals(ORGANIZATION_DTO.getName(), createdOrganization.getName());
      Assertions.assertEquals(ORGANIZATION_DTO.getRepresentativeFullName(), createdOrganization.getRepresentativeFullName());
      Assertions.assertEquals(ORGANIZATION_DTO.getColony(), createdOrganization.getColony());
      Assertions.assertEquals(ORGANIZATION_DTO.getStreet(), createdOrganization.getStreet());
      Assertions.assertInstanceOf(LocalDateTime.class, createdOrganization.getCreatedAt());
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

      OrganizationDTO createdOrganization = ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail());

      Assertions.assertNotNull(createdOrganization);
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

      OrganizationDTO organization = ORGANIZATION_DAO.getOne(ORGANIZATION_DTO.getEmail());
      Assertions.assertEquals(updatedOrganization.getName(), organization.getName());
      Assertions.assertEquals(updatedOrganization.getRepresentativeFullName(), organization.getRepresentativeFullName());
      Assertions.assertEquals(updatedOrganization.getColony(), organization.getColony());
      Assertions.assertEquals(updatedOrganization.getStreet(), organization.getStreet());
      Assertions.assertEquals(updatedOrganization.getState(), organization.getState());
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
