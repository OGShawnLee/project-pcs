package org.example.db;

import java.sql.SQLException;
import java.util.List;

public interface DAO <T> {
  void create(T element) throws SQLException;

  List<T> getAll() throws SQLException;

  T get(int id) throws SQLException;
  T get(String id) throws SQLException;

  void update(T element) throws SQLException;

  // TODO: UPDATE PARAMETER WITH DTO
  void delete(int id) throws SQLException;
  void delete(String id) throws SQLException;
}
