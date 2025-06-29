package org.example.business.dao.shape;

import org.example.business.dao.NotFoundException;
import org.example.common.UserDisplayableException;

import java.sql.SQLException;

/**
 * Interface for retrieving a single data object from the database.
 *
 * @param <T> the type of the data object to be retrieved
 * @param <F> the type of the filter used to identify the data object to be retrieved
 */
public interface GetOneDAOShape<T, F> {
  T getOne(F filter) throws UserDisplayableException, NotFoundException;
}
