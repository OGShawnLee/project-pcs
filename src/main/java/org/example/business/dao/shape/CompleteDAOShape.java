package org.example.business.dao.shape;

/**
 * CompleteDAOShape is a DAO shape that includes all CRUD operations.
 * It extends DAOShape and implements CreateOneDAOShape, GetOneDAOShape, GetAllDAOShape, UpdateOneDAOShape, and DeleteOneDAOShape.
 * This shape is used for DAOs that need to support all basic operations on a data object.
 * @param <T> the type of the data object
 * @param <F> the type of the filter used for retrieving a single data object
 */
public abstract class CompleteDAOShape<T, F> extends DAOShape<T>
  implements CreateOneDAOShape<T>, GetOneDAOShape<T, F>, GetAllDAOShape<T>, UpdateOneDAOShape<T>, DeleteOneDAOShape<F> {
}
