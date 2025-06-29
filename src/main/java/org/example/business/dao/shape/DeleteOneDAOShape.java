package org.example.business.dao.shape;

import org.example.common.UserDisplayableException;

/**
 * Interface for deleting a single data object from the database.
 *
 * @param <F> the type of the filter used to identify the data object to be deleted
 */
public interface DeleteOneDAOShape<F> {
  void deleteOne(F filter) throws UserDisplayableException;
}
