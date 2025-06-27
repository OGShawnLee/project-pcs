package db;

import org.example.business.dao.NotFoundException;
import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.RepresentativeDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepresentativeDAOTest {
  public static final RepresentativeDAO REPRESENTATIVE_DAO = new RepresentativeDAO();
  public static final RepresentativeDTO REPRESENTATIVE_DTO = new RepresentativeDTO.RepresentativeBuilder()
    .setOrganizationID(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
    .setName("Daniel")
    .setPaternalLastName("Cruise")
    .setEmail("Curise@gmail.com")
    .setPhoneNumber("4050603050")
    .setPosition("Project Manager")
    .setState("ACTIVE")
    .build();

  public static void createOneTestRepresentative() throws SQLException {
    OrganizationDAOTest.createOneTestOrganization();
    REPRESENTATIVE_DAO.createOne(REPRESENTATIVE_DTO);
  }

  public static void deleteOneTestRepresentative() throws SQLException {
    OrganizationDAOTest.deleteOneTestOrganization();
    REPRESENTATIVE_DAO.deleteOne(REPRESENTATIVE_DTO.getEmail());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestRepresentative();
  }

  @Test
  public void testCreateOneRepresentative() {
    assertDoesNotThrow(() -> {
      createOneTestRepresentative();
      RepresentativeDTO createdRepresentative = REPRESENTATIVE_DAO.getOne(REPRESENTATIVE_DTO.getEmail());
      Assertions.assertEquals(REPRESENTATIVE_DTO, createdRepresentative);
    });
  }

  @Test
  public void testGetAllRepresentatives() {
    assertDoesNotThrow(() -> {
      createOneTestRepresentative();
      List<RepresentativeDTO> representativeList = REPRESENTATIVE_DAO.getAll();
      Assertions.assertNotNull(representativeList);
      Assertions.assertFalse(representativeList.isEmpty());
    });
  }

  @Test
  public void testGetOneRepresentative() {
    assertDoesNotThrow(() -> {
      createOneTestRepresentative();
      RepresentativeDTO retrievedRepresentative = REPRESENTATIVE_DAO.getOne(REPRESENTATIVE_DTO.getEmail());
      Assertions.assertEquals(REPRESENTATIVE_DTO, retrievedRepresentative);
    });
  }

  @Test
  public void testGetOneRepresentativeNull() {
    assertThrows(
      NotFoundException.class,
      () -> REPRESENTATIVE_DAO.getOne(REPRESENTATIVE_DTO.getEmail())
    );
  }

  @Test
  public void testUpdateOneRepresentative() {
    assertDoesNotThrow(() -> {
      createOneTestRepresentative();
      RepresentativeDTO updatedRepresentative = new RepresentativeDTO.RepresentativeBuilder()
        .setOrganizationID(REPRESENTATIVE_DTO.getOrganizationID())
        .setName("John")
        .setPaternalLastName("Doe")
        .setEmail(REPRESENTATIVE_DTO.getEmail())
        .setPhoneNumber("1234567890")
        .setPosition("Senior Project Manager")
        .setState("ACTIVE")
        .build();

      REPRESENTATIVE_DAO.updateOne(updatedRepresentative);
      RepresentativeDTO retrievedRepresentative = REPRESENTATIVE_DAO.getOne(REPRESENTATIVE_DTO.getEmail());
      Assertions.assertEquals(updatedRepresentative, retrievedRepresentative);
    });
  }

  @Test
  public void testDeleteOneRepresentative() {
    assertDoesNotThrow(() -> {
      createOneTestRepresentative();
      REPRESENTATIVE_DAO.deleteOne(REPRESENTATIVE_DTO.getEmail());
      Assertions.assertThrows(NotFoundException.class, () -> {
        REPRESENTATIVE_DAO.getOne(REPRESENTATIVE_DTO.getEmail());
      });
    });
  }
}
