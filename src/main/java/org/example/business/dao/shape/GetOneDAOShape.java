package org.example.business.dao.shape;

import org.example.business.dao.NotFoundException;

import java.sql.SQLException;

public interface GetOneDAOShape<T, F> extends DAOShape<T> {
  T getOne(F filter) throws SQLException, NotFoundException;
}
