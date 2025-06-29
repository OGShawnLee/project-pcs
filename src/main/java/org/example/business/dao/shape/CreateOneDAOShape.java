package org.example.business.dao.shape;

import org.example.common.UserDisplayableException;

/**
 * Interface for creating a single data object in the database.
 *
 * @param <T> the type of the data object to be created
 */
public interface CreateOneDAOShape<T> {
  void createOne(T dataObject) throws UserDisplayableException;
}
