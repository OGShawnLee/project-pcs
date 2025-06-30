package org.example.gui.controller;

/**
 * ContextController is a generic controller that holds a context of type T.
 * It extends the base Controller class and provides methods to initialize and retrieve the context.
 * The context can be any type, allowing for flexible use in different scenarios.
 * <p>
 * This class is useful for controllers that need to operate with a specific context,
 * such as when working on an existing database record.
 *
 * @param <T> the type of the context
 */
public class ContextController<T> extends Controller {
  private T context;

  public T getContext() {
    return context;
  }

  public void initialize(T context) {
    this.context = context;
  }
}
