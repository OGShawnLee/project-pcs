package org.example.business.dao;

import java.sql.SQLException;
import java.util.List;

public interface GetOneDAOShape<T, F> extends DAOShape<T> {
  T getOne(F filter) throws SQLException, NotFoundException;
}
