package org.example.business.dao.shape;

import java.sql.SQLException;

public interface UpdateOneDAOShape<T> {
  void updateOne(T dataObject) throws SQLException;
}
