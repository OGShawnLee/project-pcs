package org.example.business.dao.shape;

import java.sql.SQLException;
import java.util.List;

public interface GetAllDAOShape<T> {
  List<T> getAll() throws SQLException;
}
