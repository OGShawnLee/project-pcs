package org.example.business.dao.shape;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DAOShape<T> {
  T createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException;
}