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
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  private final String EMAIL = "microsoft@outlook.com";
  private final String NAME = "Microsoft";
  private final String REPRESENTATIVE_FULL_NAME = "Bill Gates";
  private final String COLONY = "Redmond";
  private final String STREET = "Microsoft Way";
  private final OrganizationDTO BASE_ORGANIZATION_DTO = new OrganizationDTO.OrganizationBuilder()
    .setEmail(EMAIL)
    .setName(NAME)
    .setRepresentativeFullName(REPRESENTATIVE_FULL_NAME)
    .setColony(COLONY)
    .setStreet(STREET)
    .build();

  private void createTestData() throws SQLException {
    ORGANIZATION_DAO.create(BASE_ORGANIZATION_DTO);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    ORGANIZATION_DAO.delete(EMAIL);
  }

  @Test
  public void testCreateOrganization() {
    assertDoesNotThrow(() -> {
      createTestData();

      OrganizationDTO createdOrganization = ORGANIZATION_DAO.get(EMAIL);

      Assertions.assertNotNull(createdOrganization);
      Assertions.assertEquals(EMAIL, createdOrganization.getEmail());
      Assertions.assertEquals(NAME, createdOrganization.getName());
      Assertions.assertEquals(REPRESENTATIVE_FULL_NAME, createdOrganization.getRepresentativeFullName());
      Assertions.assertEquals(COLONY, createdOrganization.getColony());
      Assertions.assertEquals(STREET, createdOrganization.getStreet());
      Assertions.assertInstanceOf(LocalDateTime.class, createdOrganization.getCreatedAt());
    });
  }

  @Test
  public void testGetAllOrganizations() {
    assertDoesNotThrow(() -> {
      createTestData();

      List<OrganizationDTO> organizationList = ORGANIZATION_DAO.getAll();

      Assertions.assertFalse(organizationList.isEmpty());
    });
  }

  @Test
  public void testGetOrganization() {
    assertDoesNotThrow(() -> {
      createTestData();

      OrganizationDTO createdOrganization = ORGANIZATION_DAO.get(EMAIL);

      Assertions.assertNotNull(createdOrganization);
      Assertions.assertEquals(EMAIL, createdOrganization.getEmail());
      Assertions.assertEquals(NAME, createdOrganization.getName());
      Assertions.assertEquals(REPRESENTATIVE_FULL_NAME, createdOrganization.getRepresentativeFullName());
      Assertions.assertEquals(COLONY, createdOrganization.getColony());
      Assertions.assertEquals(STREET, createdOrganization.getStreet());
    });
  }

  @Test
  public void testUpdateOrganization() {
    assertDoesNotThrow(() -> {
      createTestData();

      String updatedName = "Apple Inc.";
      String updatedRepresentativeFullName = "Steve Jobs";
      String updatedColony = "Cupertino";
      String updatedStreet = "Infinite Loop";
      String updatedState = "RETIRED";

      OrganizationDTO updatedOrganization = new OrganizationDTO.OrganizationBuilder()
        .setEmail(EMAIL)
        .setName(updatedName)
        .setRepresentativeFullName(updatedRepresentativeFullName)
        .setColony(updatedColony)
        .setStreet(updatedStreet)
        .setState(updatedState)
        .build();

      ORGANIZATION_DAO.update(updatedOrganization);

      OrganizationDTO retrievedOrganization = ORGANIZATION_DAO.get(EMAIL);

      Assertions.assertEquals(updatedName, retrievedOrganization.getName());
      Assertions.assertEquals(updatedRepresentativeFullName, retrievedOrganization.getRepresentativeFullName());
      Assertions.assertEquals(updatedColony, retrievedOrganization.getColony());
      Assertions.assertEquals(updatedStreet, retrievedOrganization.getStreet());
      Assertions.assertEquals(updatedState, retrievedOrganization.getState());
    });
  }

  @Test
  public void testDeleteOrganization() {
    assertDoesNotThrow(() -> {
      createTestData();

      ORGANIZATION_DAO.delete(EMAIL);

      OrganizationDTO deletedOrganization = ORGANIZATION_DAO.get(EMAIL);

      Assertions.assertNull(deletedOrganization);
    });
  }
}
