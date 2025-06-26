package org.example.business.dao.shape;

import java.sql.SQLException;

public interface CreateOneDAOShape<T> {
  void createOne(T dataObject) throws SQLException;
}
