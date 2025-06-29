package org.example.business.dao.shape;

import org.example.common.UserDisplayableException;

/**
 * Interface for updating a single data object in the database.
 *
 * @param <T> the type of the data object to be updated
 */
public interface UpdateOneDAOShape<T> {
  void updateOne(T dataObject) throws UserDisplayableException;
}
