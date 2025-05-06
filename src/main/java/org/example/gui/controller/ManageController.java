package org.example.gui.controller;

public abstract class ManageController<T> extends Controller {
  protected abstract void initialize(T dataObject);

  protected abstract void handleUpdateCurrentDataObject();
}
