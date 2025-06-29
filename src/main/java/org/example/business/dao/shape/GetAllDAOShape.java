package org.example.business.dao.shape;

import org.example.common.UserDisplayableException;

import java.util.List;

/**
 * Interface for retrieving all data objects of a specific type from the database.
 *
 * @param <T> the type of the data objects to be retrieved
 */
public interface GetAllDAOShape<T> {
  List<T> getAll() throws UserDisplayableException;
}
