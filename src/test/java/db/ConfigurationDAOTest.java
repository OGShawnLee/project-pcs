package db;

import org.example.business.dto.enumeration.ConfigurationName;
import org.example.business.dao.ConfigurationDAO;
import org.example.business.dto.ConfigurationDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigurationDAOTest {
  public static final ConfigurationDAO CONFIGURATION_DAO = new ConfigurationDAO();
  public static final ConfigurationDTO CONFIGURATION_DTO = new ConfigurationDTO(
    ConfigurationName.EVALUATION_ENABLED_FIRST,
    true
  );

  @Test
  public void testGetAllConfigurations() {
    assertDoesNotThrow(() -> {
      List<ConfigurationDTO> configurationList = CONFIGURATION_DAO.getAll();

      assertNotNull(configurationList);
      assertEquals(3, configurationList.size());
    });
  }

  @Test
  public void testGetOneConfiguration() {
    assertDoesNotThrow(() -> {
      assertNotNull(CONFIGURATION_DAO.getOne(CONFIGURATION_DTO.name()));
    });
  }

  @Test
  public void testUpdateOneConfiguration() {
    assertDoesNotThrow(() -> {
      ConfigurationDTO updatedConfiguration = new ConfigurationDTO(ConfigurationName.EVALUATION_ENABLED_FIRST, false);
      CONFIGURATION_DAO.updateOne(updatedConfiguration);
      assertEquals(updatedConfiguration, CONFIGURATION_DAO.getOne(updatedConfiguration.name()));

      updatedConfiguration = new ConfigurationDTO(ConfigurationName.EVALUATION_ENABLED_SECOND, true);
      CONFIGURATION_DAO.updateOne(updatedConfiguration);
      assertEquals(updatedConfiguration, CONFIGURATION_DAO.getOne(updatedConfiguration.name()));

      updatedConfiguration = new ConfigurationDTO(ConfigurationName.EVALUATION_ENABLED_FINAL, false);
      CONFIGURATION_DAO.updateOne(updatedConfiguration);
      assertEquals(updatedConfiguration, CONFIGURATION_DAO.getOne(updatedConfiguration.name()));
    });
  }
}
