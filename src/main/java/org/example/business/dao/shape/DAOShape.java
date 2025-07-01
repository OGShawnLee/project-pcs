package org.example.business.dao.shape;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAOShape is an abstract class that provides a template for the basic Data Access Object (DAO) shapes.
 * It is designed to be extended by specific DAO implementations that handle different types of Data Transfer Objects (DTOs).
 * It defines methods for creating a Data Transfer Object (DTO) instance from a ResultSet,
 * handling exceptions that may occur during the process.
 *
 * @param <T> the type of the DTO that this DAOShape will handle.
 */
public abstract class DAOShape<T> {
  protected static final Logger LOGGER = LogManager.getLogger(DAOShape.class);
  /**
   * Gets a DTO instance from the ResultSet. This method is intended to be used by the createDTOInstanceFromResultSet method
   * to create a DTO instance from the ResultSet along with error handling.
   *
   * @return a DTO instance populated with data from the ResultSet.
   * @throws SQLException if there is an error accessing the ResultSet.
   */
  public abstract T getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException;

  /**
   * Creates a DTO instance from the provided ResultSet. This method builds upon the getDTOInstanceFromResultSet method
   * to create a DTO instance from the ResultSet, handling any exceptions that may occur.
   *
   * @param resultSet the ResultSet containing data from the database.
   * @return a DTO instance populated with data from the ResultSet.
   * @throws UserDisplayableException if there is an error creating the DTO instance.
   */
  public T createDTOInstanceFromResultSet(ResultSet resultSet) throws UserDisplayableException {
    try {
      return getDTOInstanceFromResultSet(resultSet);
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e);
    }
  }
}