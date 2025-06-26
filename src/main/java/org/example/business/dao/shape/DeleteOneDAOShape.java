package org.example.business.dao.shape;

import java.sql.SQLException;

public interface DeleteOneDAOShape<F> {
  void deleteOne(F filter) throws SQLException;
}
