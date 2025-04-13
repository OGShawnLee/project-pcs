package org.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO <T, F> extends DBConnector {
  protected abstract T createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException;

  public abstract void create(T dataObject) throws SQLException;

  public abstract List<T> getAll() throws SQLException;

  public abstract T get(F filter) throws SQLException;

  public abstract void update(T dataObject) throws SQLException;

  public abstract void delete(F filter) throws SQLException;
}
