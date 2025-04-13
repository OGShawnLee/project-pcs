package org.example.db;

import org.example.db.filter.Filter;

import java.sql.SQLException;
import java.util.List;

public interface DAO <T, F> {
  void create(T element) throws SQLException;

  List<T> getAll() throws SQLException;

  T get(F filter) throws SQLException;

  void update(T element) throws SQLException;

  void delete(F filter) throws SQLException;
}
