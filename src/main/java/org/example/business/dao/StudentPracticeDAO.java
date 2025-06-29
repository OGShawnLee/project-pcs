package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.DAOShape;
import org.example.business.dto.StudentPracticeDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentPracticeDAO extends DAOShape<StudentPracticeDTO> {
private static final Logger LOGGER = LogManager.getLogger(StudentPracticeDAO.class);
  private static final String GET_ALL_BY_PROJECT_ID = "SELECT * FROM StudentPractice WHERE id_project = ?";

  @Override
  public StudentPracticeDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new StudentPracticeDTO(
      new StudentDAO().getDTOInstanceFromResultSet(resultSet),
      new PracticeDAO().getDTOInstanceFromResultSet(resultSet)
    );
  }

  public List<StudentPracticeDTO> getAllByProjectID(int idProject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_PROJECT_ID);
    ) {
      statement.setInt(1, idProject);

      try (ResultSet resultSet = statement.executeQuery()) {
        List<StudentPracticeDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los estudiantes de la práctica.");
    }
  }
}
