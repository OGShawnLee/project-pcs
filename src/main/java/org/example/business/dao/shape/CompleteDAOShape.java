package org.example.business.dao.shape;

public abstract class CompleteDAOShape<T, F>
  implements DAOShape<T>, CreateOneDAOShape<T>, GetOneDAOShape<T, F>, GetAllDAOShape<T>, UpdateOneDAOShape<T>, DeleteOneDAOShape<F>
{}
