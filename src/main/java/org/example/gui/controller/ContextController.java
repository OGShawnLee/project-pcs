package org.example.gui.controller;

public class ContextController<T> extends Controller {
  private T context;

  public T getContext() {
    return context;
  }

  public void initialize(T context) {
    this.context = context;
  }
}
